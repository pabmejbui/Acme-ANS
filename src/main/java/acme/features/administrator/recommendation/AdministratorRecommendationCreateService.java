
package acme.features.administrator.recommendation;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import acme.client.components.principals.Administrator;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.entities.recommendation.Recommendation;

@GuiService
public class AdministratorRecommendationCreateService extends AbstractGuiService<Administrator, Recommendation> {

	@Autowired
	private AdministratorRecommendationRepository repository;


	public Collection<Recommendation> getAllRecommendations() {
		this.repository.deleteAllRecommendations();

		List<Recommendation> recommendations = new LinkedList<>();
		List<Flight> flights = this.repository.findAllFlights();

		// Filtramos ciudades nulas o vacías
		List<String> destinationCities = flights.stream().map(Flight::getDestinationCity).filter(city -> city != null && !city.isBlank()).distinct().toList();

		final String apiKey = "5ae2e3f221c38a28845f05b677fe582495a2263f4e4c5722a8921a94";
		final RestTemplate restTemplate = new RestTemplate();

		for (String city : destinationCities)

			try {

				// 1. Obtener coordenadas de la ciudad
				String geoUrl = "https://api.opentripmap.com/0.1/en/places/geoname?name=" + URLEncoder.encode(city, StandardCharsets.UTF_8) + "&apikey=" + apiKey;

				ResponseEntity<String> geoResponse = restTemplate.getForEntity(geoUrl, String.class);
				if (geoResponse.getBody() == null || geoResponse.getBody().isBlank())
					continue;

				JSONObject geoJSON = new JSONObject(geoResponse.getBody());
				double lat = geoJSON.getDouble("lat");
				double lon = geoJSON.getDouble("lon");

				// 2. Buscar lugares de interés cercanos
				String placesUrl = "https://api.opentripmap.com/0.1/en/places/radius?radius=1000&limit=5&lon=" + lon + "&lat=" + lat + "&apikey=" + apiKey;

				ResponseEntity<String> placesResponse = restTemplate.getForEntity(placesUrl, String.class);
				if (placesResponse.getBody() == null || placesResponse.getBody().isBlank())
					continue;

				JSONObject placesJSON = new JSONObject(placesResponse.getBody());
				JSONArray features = placesJSON.getJSONArray("features");

				for (int i = 0; i < features.length(); i++) {
					JSONObject feature = features.getJSONObject(i);
					JSONObject properties = feature.getJSONObject("properties");

					String name = properties.optString("name", "").trim();
					if (name.isBlank())
						continue;

					String kinds = properties.optString("kinds", "unknown");
					String mainType = kinds.contains(",") ? kinds.split(",")[0] : kinds;

					double rating = properties.has("rate") ? properties.getDouble("rate") : 0.0;

					Recommendation recommendation = new Recommendation();
					recommendation.setCity(city);
					recommendation.setName(name);
					recommendation.setType(mainType);
					recommendation.setRating(rating);

					this.repository.save(recommendation);
					recommendations.add(recommendation);
				}

				MomentHelper.sleep(1000);

			} catch (Exception e) {
				System.out.println("Error en ciudad '" + city + "': " + e.getMessage());
			}

		return recommendations;
	}
}
