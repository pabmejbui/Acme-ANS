
package acme.features.weather;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

	@Value("${weather.api.key}")
	private String	apiKey;

	@Value("${weather.api.url}")
	private String	apiUrl;


	public WeatherCondition getWeather(final String city, final String countryCode) {
		RestTemplate restTemplate = new RestTemplate();
		String url = String.format("%s?q=%s,%s&units=metric&appid=%s", this.apiUrl, city, countryCode, this.apiKey);

		WeatherResponse weatherResponse = restTemplate.getForObject(url, WeatherResponse.class);

		if (weatherResponse != null && weatherResponse.getMain() != null && weatherResponse.getWeather().length > 0) {
			WeatherCondition weatherCondition = new WeatherCondition();
			weatherCondition.setCity(weatherResponse.getName());
			weatherCondition.setCountry(weatherResponse.getSys().getCountry());
			weatherCondition.setTemperature(weatherResponse.getMain().getTemp());
			weatherCondition.setHumidity(weatherResponse.getMain().getHumidity());
			weatherCondition.setWindSpeed(weatherResponse.getWind().getSpeed());
			weatherCondition.setDescription(weatherResponse.getWeather()[0].getDescription());
			weatherCondition.setReportTime(new Date());

			return weatherCondition;
		}

		return null;
	}
}
