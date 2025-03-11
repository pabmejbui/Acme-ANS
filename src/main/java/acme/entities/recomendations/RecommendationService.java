
package acme.entities.recomendations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RecommendationService {

	// Definir la clave de API directamente en el servicio
	private static final String	API_KEY	= "5ae2e3f221c38a28845f05b677fe582495a2263f4e4c5722a8921a94";  // Reemplaza esto con tu clave real

	private final RestTemplate	restTemplate;
	private final ObjectMapper	objectMapper;


	// Constructor, si tu framework requiere una forma específica de inyección
	public RecommendationService(final RestTemplate restTemplate, final ObjectMapper objectMapper) {
		this.restTemplate = restTemplate;
		this.objectMapper = objectMapper;
	}

	public List<Recommendation> getRecommendations(final double latitude, final double longitude, final int radius) {
		// Construir la URL para la consulta de la API, incluyendo la clave de API
		String url = UriComponentsBuilder.fromHttpUrl("https://api.opentripmap.com/0.1/en/places/radius").queryParam("lat", latitude).queryParam("lon", longitude).queryParam("radius", radius).queryParam("apikey", RecommendationService.API_KEY)  // Usamos la clave de API directamente aquí
			.toUriString();

		// Realizar la solicitud GET
		String response = this.restTemplate.getForObject(url, String.class);

		// Convertir la respuesta JSON en objetos Recommendation
		return this.parseRecommendationsFromResponse(response);
	}

	private List<Recommendation> parseRecommendationsFromResponse(final String response) {
		List<Recommendation> recommendations = new ArrayList<>();

		try {
			JsonNode rootNode = this.objectMapper.readTree(response);
			JsonNode featuresNode = rootNode.path("features");

			for (JsonNode featureNode : featuresNode) {
				JsonNode properties = featureNode.path("properties");

				Recommendation recommendation = new Recommendation();
				recommendation.setTitle(properties.path("name").asText());
				recommendation.setDescription(properties.path("description").asText());
				recommendation.setLocation(properties.path("lat").asText() + ", " + properties.path("lon").asText());
				recommendation.setCategory(properties.path("category").asText());
				recommendation.setUrl(properties.path("url").asText());

				recommendations.add(recommendation);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return recommendations;
	}
}
