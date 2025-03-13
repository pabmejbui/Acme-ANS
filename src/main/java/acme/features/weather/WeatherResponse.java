
package acme.features.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class WeatherResponse {

	private Main		main;
	private Wind		wind;
	private Weather[]	weather;
	private Sys			sys;
	private String		name;


	@JsonIgnoreProperties(ignoreUnknown = true)
	@Getter
	@Setter
	public static class Main {

		private Double	temp;
		private Double	humidity;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Getter
	@Setter
	public static class Wind {

		private Double speed;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Getter
	@Setter
	public static class Weather {

		private String description;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Getter
	@Setter
	public static class Sys {

		private String country;
	}
}
