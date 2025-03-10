
package acme.entities.recomendations;

import org.springframework.stereotype.Service;

@Service
public class RecommendationService {

	private static final String	API_URL	= "https://api.opentripmap.com/0.1/en/places/radius";
	private static final String	API_KEY	= "TU_API_KEY"; //Esperando key me daba 502 en el register

	//	public List<Recommendation> fetchRecommendations(final double latitude, final double longitude, final int radius) {
	//		List<Recommendation> recommendations = new ArrayList<>();
	//
	//		String url = String.format("%s?radius=%d&lon=%.6f&lat=%.6f&kinds=interesting_places&apikey=%s", RecommendationService.API_URL, radius, longitude, latitude, RecommendationService.API_KEY);
	//
	//		try (CloseableHttpClient httpClient = CloseableHttpClientBuilder.create().build()) {
	//			HttpGet request = new HttpGet(url);
	//			try (CloseableHttpResponse response = httpClient.execute(request)) {
	//				ObjectMapper mapper = new ObjectMapper();
	//				JsonNode rootNode = mapper.readTree(response.getEntity().getContent());
	//
	//				if (rootNode.has("features"))
	//					for (JsonNode feature : rootNode.get("features")) {
	//						Recommendation recommendation = new Recommendation();
	//						recommendation.setTitle(feature.get("properties").get("name").asText());
	//						recommendation.setLocation(feature.get("geometry").get("coordinates").toString());
	//						recommendation.setCategory("Interesting Place");
	//						recommendation.setUrl(feature.get("properties").get("xid").asText());
	//
	//						recommendations.add(recommendation);
	//					}
	//			}
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//		return recommendations;
	//	}
}
