
package acme.entities.weather;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

	@Value("${weather.api.key}")
	private String						apiKey;

	@Value("${weather.api.url}")
	private String						apiUrl;

	@Autowired
	private WeatherConditionRepository	repository;


	public WeatherCondition getWeather(final String city) {
		RestTemplate restTemplate = new RestTemplate();
		String url = String.format("%s?q=%s&units=metric&appid=%s", this.apiUrl, city, this.apiKey);

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

			Date reportTimeTruncated = this.truncateToMinute(weatherCondition.getReportTime());

			boolean exists = this.repository.existsDuplicateCondition(weatherCondition.getCity(), weatherCondition.getCountry(), reportTimeTruncated);

			if (!exists) {
				this.repository.save(weatherCondition);
				return weatherCondition;
			} else
				throw new RuntimeException("⛔ Registro duplicado. Ya existe una condición meteorológica reciente para esta ciudad.");
		}

		throw new RuntimeException("⛔ No se pudo obtener datos del clima.");
	}

	private Date truncateToMinute(final Date date) {
		long millisPerMinute = 60 * 1000;
		long truncated = date.getTime() / millisPerMinute * millisPerMinute;
		return new Date(truncated);
	}

}
