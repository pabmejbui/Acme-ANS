
package acme.features.manager.flights;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.entities.flights.Indication;
import acme.realms.Manager;

@GuiService
public class ManagerFlightShowService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private ManagerFlightRepository repository;


	@Override
	public void authorise() {
		Flight flight = this.repository.findFlightById(super.getRequest().getData("id", int.class));
		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		super.getResponse().setAuthorised(flight.getManager().getId() == managerId);
	}

	@Override
	public void load() {
		Flight flight = this.repository.findFlightById(super.getRequest().getData("id", int.class));
		super.getBuffer().addData(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;
		SelectChoices choices;

		choices = SelectChoices.from(Indication.class, flight.getIndication());

		dataset = super.unbindObject(flight, "tag", "indication", "cost", "description", "draftMode");
		dataset.put("readonly", true);
		dataset.put("indications", choices);

		super.getResponse().addData(dataset);
	}
}
