
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.passenger.Passenger;
import acme.realms.customer.Customer;

@GuiService
public class CustomerBookingPublishService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int bookingId;
		Booking booking;
		Customer customer;

		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(bookingId);
		customer = booking == null ? null : booking.getCustomer();
		status = super.getRequest().getPrincipal().hasRealm(customer) && booking != null && booking.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking;
		int id;

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastCardNibble", "flight");
	}

	@Override
	public void validate(final Booking booking) {
		boolean hasPublishedPassengers = true;
		boolean hasCardNibble = booking.getLastCardNibble() != null;

		int id = super.getRequest().getData("id", int.class);
		Collection<Passenger> passengers = this.repository.findPassengersByBookingId(id);

		if (passengers.isEmpty())
			hasPublishedPassengers = false;
		else
			for (Passenger passenger : passengers)
				if (passenger.isDraftMode()) {
					hasPublishedPassengers = false;
					break;
				}

		super.state(hasPublishedPassengers, "*", "customer.booking.publish.not-published-passengers");
		super.state(hasCardNibble, "lastCardNibble", "customer.booking.publish.missing-card-nibble");

	}

	@Override
	public void perform(final Booking booking) {
		booking.setDraftMode(false);
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastCardNibble", "flight", "draftMode");
		dataset.put("bookingCost", booking.getCost());
		super.getResponse().addData(dataset);
	}

}
