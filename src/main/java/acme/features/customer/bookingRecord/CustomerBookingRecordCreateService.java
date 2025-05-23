
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.passenger.Passenger;
import acme.realms.customer.Customer;

@GuiService
public class CustomerBookingRecordCreateService extends AbstractGuiService<Customer, BookingRecord> {

	@Autowired
	private CustomerBookingRecordRepository repository;


	@Override
	public void authorise() {
		Boolean status = true;

		try {

			Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			Integer bookingId = super.getRequest().getData("bookingId", Integer.class);
			Booking booking = this.repository.findBookingById(bookingId);
			status = booking != null && customerId == booking.getCustomer().getId();

			if (super.getRequest().hasData("id")) {
				String locatorCode = super.getRequest().getData("locatorCode", String.class);
				status = status && booking.getLocatorCode().equals(locatorCode);

				Integer passengerId = super.getRequest().getData("passenger", Integer.class);
				Passenger passenger = null;
				if (passengerId != null)
					passenger = this.repository.findPassengerById(passengerId);
				status = status && passengerId != null && (passenger != null && customerId == passenger.getCustomer().getId() || passengerId == 0);

				Collection<Passenger> alreadyAddedPassengers = this.repository.findAllPassengersByBookingId(bookingId);
				status = status && !alreadyAddedPassengers.stream().anyMatch(p -> p.getId() == passengerId);
			}

		} catch (Throwable E) {
			status = false;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Integer bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.repository.findBookingById(bookingId);
		BookingRecord bookingRecord = new BookingRecord();
		bookingRecord.setBooking(booking);
		super.getBuffer().addData(bookingRecord);
	}

	@Override
	public void bind(final BookingRecord bookingRecord) {
		super.bindObject(bookingRecord, "passenger", "locatorCode");
	}

	@Override
	public void validate(final BookingRecord bookingRecord) {
		;

	}

	@Override
	public void perform(final BookingRecord bookingRecord) {
		this.repository.save(bookingRecord);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		Integer bookingId = super.getRequest().getData("bookingId", int.class);

		Collection<Passenger> alreadyAddedPassengers = this.repository.findAllPassengersByBookingId(bookingId);
		Collection<Passenger> noAddedPassengers = this.repository.findAllPassengersByCustomerId(customerId).stream().filter(p -> !alreadyAddedPassengers.contains(p)).toList();
		SelectChoices passengerChoices = SelectChoices.from(noAddedPassengers, "fullName", bookingRecord.getPassenger());

		Dataset dataset = super.unbindObject(bookingRecord, "passenger", "booking");
		dataset.put("locatorCode", bookingRecord.getBooking().getLocatorCode());
		dataset.put("passengers", passengerChoices);

		super.getResponse().addData(dataset);

	}

}
