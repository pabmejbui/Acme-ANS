
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
	}

	@Override
	public void perform(final Flight flight) {
		this.repository.delete(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset = super.unbindObject(flight, "tag", "selfTransfer", "cost", "description", "draftMode");
		dataset.put("flightId", flight.getId());
		super.getResponse().addData(dataset);
	}
}
