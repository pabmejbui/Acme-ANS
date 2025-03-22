
package acme.features.any.weather;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;

@GuiService
public class AnyFlightWithBadWeatherListService extends AbstractGuiService<Any, Flight> {

	@Autowired
	private AnyWeatherRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		final Calendar cal = Calendar.getInstance();
		final Date end = cal.getTime();

		cal.add(Calendar.MONTH, -1);
		final Date start = cal.getTime();

		final int maxDiffMinutes = 60; // Puedes ajustar esto según lo consideres oportuno (± 60 minutos)

		Collection<Flight> flights;
		flights = this.repository.findFlightsWithBadWeather(start, end, maxDiffMinutes);
		super.getBuffer().addData(flights);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;
		dataset = super.unbindObject(flight, "flightNumber", "departureTime", "arrivalTime", "origin", "destination");
		super.getResponse().addData(dataset);
	}
}
