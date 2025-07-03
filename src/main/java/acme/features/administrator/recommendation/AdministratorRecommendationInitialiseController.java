
package acme.features.administrator.recommendation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import acme.client.components.principals.Administrator;
import acme.client.controllers.GuiController;
import acme.client.helpers.Assert;
import acme.client.helpers.PrincipalHelper;
import acme.client.helpers.SpringHelper;
import acme.entities.airports.Airport;
import acme.entities.recomendations.Recommendation;

@GuiController
public class AdministratorRecommendationInitialiseController {

	@Autowired
	protected AdministratorRecommendationRepository	repository;

	private final RestTemplate						httpClient						= new RestTemplate();
	private final ObjectMapper						jsonParser						= new ObjectMapper();

	private static final String						GEOCODING_SERVICE_URL			= "https://nominatim.openstreetmap.org/search?format=json&q={cityName},{countryName}";
	private static final String						OVERPASS_PROCESSOR_URL			= "http://overpass-api.de/api/interpreter";
	private static final double						DISCOVERY_RADIUS_METERS			= 5000.0;
	private static final int						MAX_RECOMMENDATIONS_PER_AIRPORT	= 10;


	@GetMapping("/administrator/system/initialise-recommendation")
	public ModelAndView startRecommendationInitializationProcess() {

		Assert.state(PrincipalHelper.get().hasRealmOfType(Administrator.class), "acme.default.error.not-authorised");

		ModelAndView resultView;

		try {
			int insertedCount = this.performRecommendationInitialization();

			resultView = new ModelAndView("fragments/welcome");
			resultView.addObject("_globalSuccessMessage", String.format("Proceso de inicialización completado: %d recomendaciones insertadas/actualizadas.", insertedCount));

		} catch (final Throwable oops) {
			resultView = new ModelAndView("master/panic");
			resultView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			resultView.addObject("_oops", oops);
			resultView.addObject("_globalErrorMessage", "acme.default.global.message.error");
		}

		return resultView;
	}

	protected List<Recommendation> generateMockPoiData(final Airport targetAirport) {
		List<Recommendation> mockRecommendations = new ArrayList<>();

		if ("Sevilla".equalsIgnoreCase(targetAirport.getCity())) {
			Recommendation r1 = new Recommendation();
			r1.setName("Real Alcázar de Sevilla");
			r1.setCategory("Historical Palace");
			r1.setAddress("Patio de Banderas, s/n, 41004 Sevilla, Spain");
			r1.setCity("Sevilla");
			r1.setCountry("Spain");
			r1.setLatitude(37.3826);
			r1.setLongitude(-5.9926);
			r1.setWebsite("https://www.alcazarsevilla.org/en/");
			r1.setAirport(targetAirport);
			mockRecommendations.add(r1);

			Recommendation r2 = new Recommendation();
			r2.setName("Catedral de Sevilla y La Giralda");
			r2.setCategory("Cathedral / Bell Tower");
			r2.setAddress("Av. de la Constitución, s/n, 41004 Sevilla, Spain");
			r2.setCity("Sevilla");
			r2.setCountry("Spain");
			r2.setLatitude(37.3862);
			r2.setLongitude(-5.9929);
			r2.setWebsite("https://www.catedraldesevilla.es/");
			r2.setAirport(targetAirport);
			mockRecommendations.add(r2);
		}

		return mockRecommendations;
	}

	protected int performRecommendationInitialization() throws InterruptedException, IOException {
		List<Airport> allAirports = this.repository.findAllAirports();
		int totalInsertedCount = 0;

		//Controla para usar api o mock (modificar dependiendo de donde se quiere ejecutar)
		boolean useMock = SpringHelper.isRunningOn("development") || SpringHelper.isRunningOn("tester") || SpringHelper.isRunningOn("tester#replay");

		for (Airport currentAirport : allAirports)
			if (currentAirport.getCity() != null && !currentAirport.getCity().isEmpty() && !currentAirport.getCity().toLowerCase().contains("lorem")) {

				List<Recommendation> recommendationsToProcess = useMock ? this.generateMockPoiData(currentAirport) : this.fetchRecommendationsFromOpenAPIs(currentAirport);

				for (Recommendation recommendation : recommendationsToProcess)
					if (!this.repository.existsByNameAndAirport(recommendation.getName(), currentAirport.getId())) {
						this.repository.save(recommendation);
						totalInsertedCount++;
					}

				if (!useMock)
					Thread.sleep(1000);
			}

		return totalInsertedCount;
	}

	private List<Recommendation> fetchRecommendationsFromOpenAPIs(final Airport targetAirport) throws IOException {
		double[] geoCoordinates = this.fetchCityCoordinates(targetAirport.getCity(), targetAirport.getCountry());
		if (geoCoordinates == null) {
			System.err.println("No se pudieron obtener coordenadas geográficas para: " + targetAirport.getCity() + ", " + targetAirport.getCountry());
			return Collections.emptyList();
		}

		double geoLat = geoCoordinates[0];
		double geoLon = geoCoordinates[1];

		return this.retrievePoisFromOverpass(geoLat, geoLon, targetAirport);
	}

	private double[] fetchCityCoordinates(final String cityName, final String countryName) {
		String requestUrl = AdministratorRecommendationInitialiseController.GEOCODING_SERVICE_URL.replace("{cityName}", cityName).replace("{countryName}", countryName);
		try {
			String jsonResponseData = this.httpClient.getForObject(requestUrl, String.class);
			JsonNode rootNode = this.jsonParser.readTree(jsonResponseData);
			if (rootNode.isArray() && rootNode.size() > 0) {
				JsonNode firstEntry = rootNode.get(0);
				double latitudeVal = firstEntry.get("lat").asDouble();
				double longitudeVal = firstEntry.get("lon").asDouble();
				return new double[] {
					latitudeVal, longitudeVal
				};
			}
		} catch (Exception e) {
			System.err.println("Error al obtener coordenadas con el servicio de geocodificación: " + e.getMessage());
		}
		return null;
	}

	private List<Recommendation> retrievePoisFromOverpass(final double currentLat, final double currentLon, final Airport associatedAirport) throws IOException {
		String overpassQueryPayload = String.format(
			"[out:json];" + "(" + "  node[\"amenity\"~\"^(cafe|restaurant|pub|bar)$\"](around:%f,%f,%f);" + "  node[\"tourism\"=\"hotel\"](around:%f,%f,%f);" + "  way[\"amenity\"~\"^(cafe|restaurant|pub|bar)$\"](around:%f,%f,%f);"
				+ "  way[\"tourism\"=\"hotel\"](around:%f,%f,%f);" + "  relation[\"amenity\"~\"^(cafe|restaurant|pub|bar)$\"](around:%f,%f,%f);" + "  relation[\"tourism\"=\"hotel\"](around:%f,%f,%f);" + ");" + "out center qt %d;",
			AdministratorRecommendationInitialiseController.DISCOVERY_RADIUS_METERS, currentLat, currentLon, AdministratorRecommendationInitialiseController.DISCOVERY_RADIUS_METERS, currentLat, currentLon,
			AdministratorRecommendationInitialiseController.DISCOVERY_RADIUS_METERS, currentLat, currentLon, AdministratorRecommendationInitialiseController.DISCOVERY_RADIUS_METERS, currentLat, currentLon,
			AdministratorRecommendationInitialiseController.DISCOVERY_RADIUS_METERS, currentLat, currentLon, AdministratorRecommendationInitialiseController.DISCOVERY_RADIUS_METERS, currentLat, currentLon,
			AdministratorRecommendationInitialiseController.MAX_RECOMMENDATIONS_PER_AIRPORT);

		try {
			String rawJsonResult = this.httpClient.postForObject(AdministratorRecommendationInitialiseController.OVERPASS_PROCESSOR_URL, overpassQueryPayload, String.class);
			JsonNode parsedRoot = this.jsonParser.readTree(rawJsonResult);
			JsonNode elementCollection = parsedRoot.path("elements");

			if (elementCollection.isArray())
				return this.parseOverpassApiResponse(elementCollection, associatedAirport);
		} catch (Exception e) {
			System.err.println("Error al consultar el procesador Overpass API: " + e.getMessage());
			throw new IOException("Fallo al obtener POIs de Overpass API", e);
		}
		return Collections.emptyList();
	}

	private List<Recommendation> parseOverpassApiResponse(final JsonNode apiElements, final Airport linkedAirport) {
		List<Recommendation> preFilteredList = new ArrayList<>();
		StreamSupport.stream(apiElements.spliterator(), false).forEach(elementNode -> {
			Recommendation poiRecommendation = new Recommendation();
			poiRecommendation.setAirport(linkedAirport);
			poiRecommendation.setCity(linkedAirport.getCity());
			poiRecommendation.setCountry(linkedAirport.getCountry());

			JsonNode poiTags = elementNode.path("tags");

			poiRecommendation.setName(poiTags.path("name").asText("Nombre Desconocido"));

			String amenityType = poiTags.path("amenity").asText("");
			String tourismType = poiTags.path("tourism").asText("");
			String shopType = poiTags.path("shop").asText("");

			if (!amenityType.isEmpty())
				poiRecommendation.setCategory(amenityType);
			else if (!tourismType.isEmpty())
				poiRecommendation.setCategory(tourismType);
			else if (!shopType.isEmpty())
				poiRecommendation.setCategory(shopType);
			else
				poiRecommendation.setCategory("Punto de Interés Genérico");

			String streetName = poiTags.path("addr:street").asText("");
			String houseNum = poiTags.path("addr:housenumber").asText("");
			String postalCode = poiTags.path("addr:postcode").asText("");
			String fullAddress = Stream.of(streetName, houseNum, postalCode).filter(s -> !s.isEmpty()).collect(Collectors.joining(", "));
			poiRecommendation.setAddress(fullAddress.isEmpty() ? poiTags.path("addr:full").asText("") : fullAddress);

			poiRecommendation.setWebsite(poiTags.path("website").asText(poiTags.path("contact:website").asText("")));

			Double lat = null;
			Double lon = null;

			if (elementNode.has("lat") && elementNode.has("lon")) {
				lat = elementNode.get("lat").asDouble();
				lon = elementNode.get("lon").asDouble();
			} else if (elementNode.has("center") && elementNode.path("center").has("lat") && elementNode.path("center").has("lon")) {
				lat = elementNode.path("center").get("lat").asDouble();
				lon = elementNode.path("center").get("lon").asDouble();
			}
			poiRecommendation.setLatitude(lat);
			poiRecommendation.setLongitude(lon);

			preFilteredList.add(poiRecommendation);
		});

		List<Recommendation> finalFilteredList = preFilteredList.stream().filter(rec -> {
			boolean hasCoords = rec.getLatitude() != null && rec.getLongitude() != null;
			return hasCoords;
		}).collect(Collectors.toList());

		return finalFilteredList;
	}
}
