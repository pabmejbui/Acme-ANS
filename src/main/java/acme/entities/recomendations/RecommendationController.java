
package acme.entities.recomendations;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RecommendationController {

	@Autowired
	private RecommendationService recommendationService;


	@GetMapping("/recommendations")
	public void getRecommendations(@RequestParam final double latitude, @RequestParam final double longitude, @RequestParam final int radius) throws JSONException {
		// Aquí solo llamas al servicio, pero no necesitas devolver nada
		this.recommendationService.getRecommendations(latitude, longitude);
	}
}
