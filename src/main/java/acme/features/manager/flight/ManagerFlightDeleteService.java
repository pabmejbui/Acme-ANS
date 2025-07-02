
package acme.features.manager.flight;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.realms.manager.Manager;

@GuiService
public class ManagerFlightDeleteService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private ManagerFlightRepository repository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findFlightById(id);

		boolean isAuthorised = flight != null && flight.isDraftMode() && super.getRequest().getPrincipal().hasRealm(flight.getManager());

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findFlightById(id);
		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
	}

	@Override
	public void validate(final Flight flight) {
		if (this.repository.existsBookingsForFlight(flight.getId()))
			super.state(false, "*", "manager.flight.delete.error.flight-linked-bookings");

		if (this.repository.existsLegsForFlight(flight.getId()))
			super.state(false, "*", "manager.flight.delete.error.flight-linked-legs");

	}

	@Override
	public void perform(final Flight flight) {
		this.repository.delete(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset = super.unbindObject(flight, "tag", "selfTransfer", "cost", "description", "draftMode");
		dataset.put("scheduledDeparture", flight.getScheduledDeparture());
		dataset.put("scheduledArrival", flight.getScheduledArrival());
		dataset.put("originCity", flight.getOriginCity());
		dataset.put("destinationCity", flight.getDestinationCity());
		dataset.put("numberOfLayovers", flight.getNumberOfLayovers());
		super.getResponse().addData(dataset);
	}

}
