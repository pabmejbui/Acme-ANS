
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.BookingRecord;
import acme.entities.passenger.Passenger;
import acme.realms.customer.Customer;

@GuiService
public class CustomerPassengerDeleteService extends AbstractGuiService<Customer, Passenger> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getMethod().equals("POST");

		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Integer passengerId = super.getRequest().getData("id", Integer.class);
		Passenger passenger = this.repository.findPassengerById(passengerId);
		status = status && customerId == passenger.getCustomer().getId() && passenger.isDraftMode();

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
		super.bindObject(passenger);
	}

	@Override
	public void validate(final Passenger passenger) {
		Collection<BookingRecord> bookingRecordsOfPassenger = this.repository.findCustomerByPassengerId(passenger.getId());
		super.state(bookingRecordsOfPassenger.isEmpty(), "*", "customer.passenger.form.error.assBookings");
	}

	@Override
	public void perform(final Passenger passenger) {
		this.repository.delete(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");
		super.getResponse().addData(dataset);
	}

}
