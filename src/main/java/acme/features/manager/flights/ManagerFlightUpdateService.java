
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
public class ManagerFlightUpdateService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private ManagerFlightRepository	repository;

	private Flight					flight;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		this.flight = this.repository.findFlightByIdAndManagerId(id, super.getRequest().getPrincipal().getActiveRealm().getId());
		boolean authorised = this.flight != null && this.flight.isDraftMode();
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		super.getBuffer().addData(this.flight);
	}

	@Override
	public void bind(final Flight flight) {
		super.bindObject(flight, "tag", "indication", "cost", "description");
	}

	@Override
	public void validate(final Flight flight) {
		super.state(flight.isDraftMode(), "*", "manager.flight.form.error.published");

		boolean tagExists = this.repository.existsFlightByTagExceptId(flight.getTag(), flight.getId());
		super.state(!tagExists, "tag", "manager.flight.form.error.tag.duplicate");
	}

	@Override
	public void perform(final Flight flight) {
		flight.setDraftMode(true);
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		SelectChoices choices = SelectChoices.from(Indication.class, flight.getIndication());
		Dataset dataset = super.unbindObject(flight, "tag", "indication", "cost", "description", "draftMode");

		dataset.put("indications", choices);
		dataset.put("command", "update");
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}
}
