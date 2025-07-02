
package acme.entities.weather;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

	@Autowired
	private WeatherService weatherService;


	@GetMapping("/weather")
	public Object getWeather(@RequestParam final String city, @RequestParam final String country) {
		try {
			return this.weatherService.getWeather(city, country);
		} catch (RuntimeException ex) {
			return Map.of("message", ex.getMessage());
		}
	}

}
