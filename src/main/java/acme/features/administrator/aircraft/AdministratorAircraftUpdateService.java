
package acme.features.administrator.aircraft;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.aircrafts.AircraftStatus;

@GuiService
public class AdministratorAircraftUpdateService extends AbstractGuiService<Administrator, Aircraft> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAircraftRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Aircraft aircraft;

		aircraft = new Aircraft();

		super.getBuffer().addData(aircraft);
	}

	@Override
	public void bind(final Aircraft aircraft) {

		super.bindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details");

	}

	@Override
	public void validate(final Aircraft aircraft) {
		boolean confirmationCreate;

		confirmationCreate = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmationCreate, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Aircraft aircraft) {
		this.repository.save(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(AircraftStatus.class, aircraft.getStatus());

		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details");
		dataset.put("status", choices.getSelected().getKey());
		dataset.put("statuses", choices);

		super.getResponse().addData(dataset);
	}

}
