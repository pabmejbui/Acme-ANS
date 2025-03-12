
package acme.entities.recomendations;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RecommendationService {

	private static final String	API_KEY		= "5ae2e3f221c38a28845f05b677fe582495a2263f4e4c5722a8921a94";
	private static final String	BASE_URL	= "https://api.opentripmap.com/0.1/en/places/radius?radius=5000&lon={lon}&lat={lat}&apikey=" + RecommendationService.API_KEY;

	private final RestTemplate	restTemplate;


	public RecommendationService(final RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public List<Recommendation> getRecommendations(final double lat, final double lon) throws JSONException {
		String url = RecommendationService.BASE_URL.replace("{lon}", String.valueOf(lon)).replace("{lat}", String.valueOf(lat));

		ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
		List<Recommendation> recommendations = new ArrayList<>();

		if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
			JSONObject jsonResponse = new JSONObject(response.getBody());

			if (jsonResponse.has("features")) {
				JSONArray places = jsonResponse.getJSONArray("features");

				for (int i = 0; i < places.length(); i++) {
					JSONObject place = places.getJSONObject(i).getJSONObject("properties");

					String name = place.optString("name", "Unknown");
					String description = place.optString("kinds", "No description available");
					String category = description.split(",")[0]; // Tomamos la primera categoría
					String location = lat + ", " + lon;

					System.out.println(name + "*************" + description + "*************" + category + "*************" + location);
				}
			}
		}
		return recommendations;
	}
}
