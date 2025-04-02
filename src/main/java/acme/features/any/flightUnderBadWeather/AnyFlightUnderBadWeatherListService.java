
package acme.features.any.flightUnderBadWeather;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.entities.weather.WeatherCondition;
import acme.features.any.weather.AnyWeatherRepository;

@GuiService
public class AnyFlightUnderBadWeatherListService extends AbstractGuiService<Any, Flight> {

	@Autowired
	private AnyFlightUnderBadWeatherRepository	repository;

	@Autowired
	private AnyWeatherRepository				weatherRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Flight> allFlights = this.repository.findAllPublishedFlights();

		Date now = MomentHelper.getCurrentMoment();
		Date cutoffDate = MomentHelper.deltaFromMoment(now, -30, java.time.temporal.ChronoUnit.DAYS);

		Collection<WeatherCondition> weatherData = this.weatherRepository.findAllWeatherConditions();

		Collection<Flight> result = allFlights.stream().filter(flight -> {
			Date departure = flight.getScheduledDeparture();
			Date arrival = flight.getScheduledArrival();

			if (departure == null || arrival == null)
				return false;

			boolean isRecent = !departure.before(cutoffDate) || !arrival.before(cutoffDate);

			boolean badWeatherAtOrigin = weatherData.stream().anyMatch(wc -> wc.getCity().equalsIgnoreCase(flight.getOriginCity()) && wc.getReportTime().before(departure) && (wc.getTemperature() < 5.0 || wc.getHumidity() > 85 || wc.getWindSpeed() > 11.1));

			boolean badWeatherAtDestination = weatherData.stream()
				.anyMatch(wc -> wc.getCity().equalsIgnoreCase(flight.getDestinationCity()) && wc.getReportTime().before(arrival) && (wc.getTemperature() < 5.0 || wc.getHumidity() > 85 || wc.getWindSpeed() > 11.1));

			return isRecent && (badWeatherAtOrigin || badWeatherAtDestination);
		}).collect(Collectors.toList());

		super.getBuffer().addData(result);
	}

	@Override
	public void unbind(final Flight object) {
		Dataset dataset = super.unbindObject(object, "tag", "description", "cost", "selfTransfer");
		dataset.put("scheduledDeparture", object.getScheduledDeparture());
		dataset.put("scheduledArrival", object.getScheduledArrival());
		dataset.put("originCity", object.getOriginCity());
		dataset.put("destinationCity", object.getDestinationCity());
		super.getResponse().addData(dataset);
	}
}
