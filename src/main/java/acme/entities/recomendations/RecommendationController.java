
package acme.entities.recomendations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import acme.client.controllers.RestController;

@RestController
public class RecommendationController {

	// Inyectamos el servicio en el controlador
	@Autowired
	private RecommendationService recommendationService;


	// Endpoint para obtener recomendaciones
	@GetMapping("/recommendations")
	public List<Recommendation> getRecommendations(@RequestParam final double latitude, @RequestParam final double longitude, @RequestParam final int radius) {
		return this.recommendationService.getRecommendations(latitude, longitude, radius);
	}
}
