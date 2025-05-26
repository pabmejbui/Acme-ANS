
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.passenger.Passenger;
import acme.realms.customer.Customer;

@GuiService
public class CustomerPassengerListService extends AbstractGuiService<Customer, Passenger> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;

		if (!super.getRequest().getData().isEmpty()) {
			Integer bookingId = super.getRequest().getData("bookingId", Integer.class);
			Booking booking = this.repository.findBookingById(bookingId);
			Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			status = booking.getCustomer().getId() == customerId;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Passenger> passengers;
		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		if (!super.getRequest().getData().containsKey("bookingId"))
			passengers = this.repository.findPassengersByCustomer(customerId);
		else {
			Integer bookingId = super.getRequest().getData("bookingId", int.class);
			passengers = this.repository.findPassengersByBookingId(bookingId);
		}
		super.getBuffer().addData(passengers);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");

		super.getResponse().addData(dataset);
		super.addPayload(dataset, passenger, "specialNeeds");
	}

}
