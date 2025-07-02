
package acme.features.any.flightUnderBadWeather;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import acme.client.controllers.GuiController;
import acme.client.helpers.MomentHelper;
import acme.entities.flights.Flight;
import acme.entities.weather.WeatherCondition;
import acme.features.any.weather.AnyWeatherRepository;

@GuiController
public class AnyFlightUnderBadWeatherInitialiseController {

	@Autowired
	private AnyFlightUnderBadWeatherRepository	flightRepository;

	@Autowired
	private AnyWeatherRepository				weatherRepository;


	@GetMapping("/any/flight-under-bad-weather/list")
	public ModelAndView listFlightsUnderBadWeather() {
		ModelAndView result;

		try {
			Date now = MomentHelper.getCurrentMoment();
			Date cutoffDate = MomentHelper.deltaFromMoment(now, -30, java.time.temporal.ChronoUnit.DAYS);

			Collection<Flight> allFlights = this.flightRepository.findAllPublishedFlights();
			Collection<WeatherCondition> allWeather = this.weatherRepository.findAllWeatherConditions();

			List<Flight> resultFlights = allFlights.stream().filter(flight -> {
				Date departure = flight.getScheduledDeparture();
				Date arrival = flight.getScheduledArrival();

				if (departure == null || arrival == null)
					return false;

				boolean recent = !departure.before(cutoffDate) || !arrival.before(cutoffDate);

				// Añadir margen de tolerancia de 30 minutos
				Date originStart = MomentHelper.deltaFromMoment(departure, -30, java.time.temporal.ChronoUnit.MINUTES);
				Date destStart = MomentHelper.deltaFromMoment(arrival, -30, java.time.temporal.ChronoUnit.MINUTES);

				String origin = this.normalize(flight.getOriginCity());
				String destination = this.normalize(flight.getDestinationCity());

				boolean badOrigin = allWeather.stream().anyMatch(w -> this.normalize(w.getCity()).equals(origin) && !w.getReportTime().before(originStart) && !w.getReportTime().after(departure)
					&& (w.getTemperature() <= 5.0 || w.getTemperature() >= 37.0 || w.getDescription().toLowerCase().matches(".*(rain|storm|snow|thunder).*")));

				boolean badDestination = allWeather.stream().anyMatch(w -> this.normalize(w.getCity()).equals(destination) && !w.getReportTime().before(destStart) && !w.getReportTime().after(arrival)
					&& (w.getTemperature() <= 5.0 || w.getTemperature() >= 37.0 || w.getDescription().toLowerCase().matches(".*(rain|storm|snow|thunder).*")));

				// DEBUG opcional
				System.out.printf("[DEBUG] Flight %s -> %s | dep: %s arr: %s | badOrigin: %s | badDest: %s\n", flight.getOriginCity(), flight.getDestinationCity(), departure, arrival, badOrigin, badDestination);

				return recent && (badOrigin || badDestination);
			}).collect(Collectors.toList());

			result = new ModelAndView("any/flight-under-bad-weather/list");
			result.addObject("list", resultFlights);

		} catch (final Throwable oops) {
			result = new ModelAndView("master/panic");
			result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			result.addObject("_oops", oops);
			result.addObject("_globalErrorMessage", "acme.default.global.message.error");
		}

		return result;
	}

	private String normalize(final String input) {
		return input == null ? "" : input.trim().replaceAll("[^\\p{ASCII}]", "").toLowerCase();
	}

}
