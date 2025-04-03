
package acme.features.administrator.aircraft;

<<<<<<< HEAD
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.aircrafts.AircraftStatus;
import acme.entities.airlines.Airline;

@GuiService
public class AdministratorAircraftCreateService extends AbstractGuiService<Administrator, Aircraft> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAircraftRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Aircraft aircraft;

		aircraft = new Aircraft();

		super.getBuffer().addData(aircraft);
	}

	@Override
	public void bind(final Aircraft aircraft) {
		int airlineId;
		Airline airline;

		airlineId = super.getRequest().getData("airline", int.class);
		airline = this.repository.findAirlineById(airlineId);

		super.bindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details");
		aircraft.setAirline(airline);

	}

	@Override
	public void validate(final Aircraft aircraft) {
		boolean confirmationCreate;

		confirmationCreate = super.getRequest().getData("confirmationCreate", boolean.class);
		super.state(confirmationCreate, "confirmationCreate", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Aircraft aircraft) {

		Aircraft a = this.repository.findAircraftById(aircraft.getId());
		a.setModel(aircraft.getModel());
		a.setRegistrationNumber(aircraft.getRegistrationNumber());
		a.setCapacity(aircraft.getCapacity());
		a.setCargoWeight(aircraft.getCargoWeight());
		a.setStatus(aircraft.getStatus());
		a.setDetails(aircraft.getDetails());
		a.setAirline(aircraft.getAirline());
		this.repository.save(a);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		SelectChoices choices;
		SelectChoices airlineChoices;
		Dataset dataset;

		Collection<Airline> airlines = this.repository.findAllAirline();
		airlineChoices = SelectChoices.from(airlines, "id", aircraft.getAirline());
		choices = SelectChoices.from(AircraftStatus.class, aircraft.getStatus());

		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details");
		dataset.put("aircraftStatus", choices);
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("airlines", airlineChoices);
		dataset.put("airline", airlineChoices.getSelected().getKey());
=======
import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.aircrafts.AircraftStatus;

@GuiService
public class AdministratorAircraftCreateService extends AbstractGuiService<Administrator, Aircraft> {

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

		confirmationCreate = super.getRequest().getData("confirmationCreate", boolean.class);
		super.state(confirmationCreate, "confirmationCreate", "acme.validation.confirmation.message");
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
>>>>>>> refs/heads/master

		super.getResponse().addData(dataset);
	}

}
