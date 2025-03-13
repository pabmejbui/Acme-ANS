
package acme.features.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

	@Autowired
	private WeatherService weatherService;


	@GetMapping("/weather")
	public WeatherCondition getWeather(@RequestParam final String city, @RequestParam final String country) {
		return this.weatherService.getWeather(city, country);
	}
}
