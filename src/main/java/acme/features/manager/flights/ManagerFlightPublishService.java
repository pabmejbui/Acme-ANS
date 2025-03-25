
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
public class ManagerFlightPublishService extends AbstractGuiService<Manager, Flight> {

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
		int totalLegs = this.repository.countLegsByFlightId(flight.getId());
		super.state(totalLegs > 0, "*", "manager.flight.form.error.noLegs");

		int landedOrCancelled = this.repository.countLandedOrCancelledLegsByFlightId(flight.getId());
		super.state(landedOrCancelled == totalLegs, "*", "manager.flight.form.error.legsNotFinalised");

		super.state(flight.isDraftMode(), "*", "manager.flight.form.error.published");

	}

	@Override
	public void perform(final Flight flight) {
		flight.setDraftMode(false);
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(Indication.class, flight.getIndication());

		dataset = super.unbindObject(flight, "tag", "indication", "cost", "description", "draftMode");
		dataset.put("readonly", true); // o false según corresponda
		dataset.put("indications", choices);

		super.getResponse().addData(dataset);
	}
}
