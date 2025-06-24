
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.realms.customer.Customer;

@GuiService
public class CustomerPassengerUpdateService extends AbstractGuiService<Customer, Passenger> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;

		Integer passengerId = super.getRequest().getData("id", Integer.class);
		Passenger passenger = this.repository.findPassengerById(passengerId);

		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		status = passenger.getCustomer().getId() == customerId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Passenger passenger;
		int id;

		id = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(id);

		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds");
	}

	@Override
	public void validate(final Passenger passenger) {
		Integer customerId = passenger.getCustomer().getId();
		String passportNumber = passenger.getPassportNumber();
		Integer currentPassengerId = passenger.getId();

		Collection<Passenger> existingPassengers = this.repository.findPassengersByCustomerAndPassportNumber(customerId, passportNumber);

		boolean isPassportDuplicated = false;

		for (Passenger existingPassenger : existingPassengers)
			if (!(existingPassenger.getId() == currentPassengerId)) {
				isPassportDuplicated = true;
				break;
			}

		super.state(!isPassportDuplicated, "passportNumber", "customer.passenger.form.error.duplicatePassportNumber");
	}

	@Override
	public void perform(final Passenger passenger) {
		this.repository.save(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");
		super.getResponse().addData(dataset);
	}
}
