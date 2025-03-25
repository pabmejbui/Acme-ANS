
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
public class ManagerFlightCreateService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private ManagerFlightRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Flight flight = new Flight();
		flight.setDraftMode(true);
		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		super.bindObject(flight, "tag", "indication", "cost", "description");
	}

	@Override
	public void validate(final Flight flight) {
		boolean tagExists = this.repository.existsFlightByTag(flight.getTag());
		super.state(!tagExists, "tag", "manager.flight.form.error.tag.duplicate");
	}

	@Override
	public void perform(final Flight flight) {
		flight.setDraftMode(true);
		Manager manager = this.repository.findManagerById(super.getRequest().getPrincipal().getActiveRealm().getId());
		flight.setManager(manager);
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(Indication.class, flight.getIndication());

		dataset = super.unbindObject(flight, "tag", "indication", "cost", "description", "draftMode");
		dataset.put("readonly", false);
		dataset.put("indications", choices);

		super.getResponse().addData(dataset);
	}
}
