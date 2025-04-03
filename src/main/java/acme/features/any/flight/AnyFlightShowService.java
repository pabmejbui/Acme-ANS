
package acme.features.any.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.entities.flights.Leg;

@GuiService
public class AnyFlightShowService extends AbstractGuiService<Any, Flight> {

	@Autowired
	private AnyFlightRepository repository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findOneFlightById(id);
		super.getResponse().setAuthorised(flight != null && !flight.isDraftMode());
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findOneFlightById(id);
		super.getBuffer().addData(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Collection<Leg> legs = this.repository.findLegsByFlightId(flight.getId());

		Dataset dataset = super.unbindObject(flight, "tag", "description", "selfTransfer", "cost");
		dataset.put("originCity", flight.getOriginCity());
		dataset.put("destinationCity", flight.getDestinationCity());
		dataset.put("scheduledDeparture", flight.getScheduledDeparture());
		dataset.put("scheduledArrival", flight.getScheduledArrival());
		dataset.put("numberOfLayovers", flight.getNumberOfLayovers());

		// Formateo legible de las escalas (legs)
		StringBuilder formattedLegs = new StringBuilder();
		String pattern = "dd/MM/yyyy HH:mm";

		for (Leg leg : legs) {
			String departure = MomentHelper.format(leg.getScheduledDeparture(), pattern);
			String arrival = MomentHelper.format(leg.getScheduledArrival(), pattern);
			formattedLegs.append(String.format("• %s – From %s to %s | %s → %s%n", leg.getFlightNumber(), leg.getOriginAirport().getCity(), leg.getDestinationAirport().getCity(), departure, arrival));
		}

		dataset.put("legsText", formattedLegs.toString());

		super.getResponse().addData(dataset);
	}
}
