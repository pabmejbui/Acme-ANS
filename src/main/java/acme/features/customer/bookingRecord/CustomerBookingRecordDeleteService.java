
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
public class CustomerBookingRecordDeleteService extends AbstractGuiService<Customer, BookingRecord> {

	@Autowired
	private CustomerBookingRecordRepository repository;


	@Override
	public void authorise() {
		Boolean status = true;

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
			status = status && (alreadyAddedPassengers.stream().anyMatch(p -> p.getId() == passengerId) || passengerId == 0);
		}

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Integer bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.repository.findBookingById(bookingId);
		BookingRecord bookingPassenger = new BookingRecord();
		bookingPassenger.setBooking(booking);
		super.getBuffer().addData(bookingPassenger);
	}

	@Override
	public void bind(final BookingRecord bookingRecord) {
		super.bindObject(bookingRecord, "passenger");

	}

	@Override
	public void validate(final BookingRecord bookingRecord) {
		Integer passengerId = super.getRequest().getData("passenger", int.class);
		super.state(passengerId != 0, "passenger", "acme.validation.noChoice");
	}

	@Override
	public void perform(final BookingRecord bookingRecord) {
		BookingRecord realBookingPassenger = this.repository.findBookingPassengerByBothIds(bookingRecord.getBooking().getId(), bookingRecord.getPassenger().getId());

		this.repository.delete(realBookingPassenger);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset;

		dataset = super.unbindObject(bookingRecord, "passenger", "booking", "id");

		Integer bookingId = super.getRequest().getData("bookingId", int.class);
		Collection<Passenger> addedPassengers = this.repository.findAllPassengersByBookingId(bookingId);
		SelectChoices passengerChoices = null;

		passengerChoices = SelectChoices.from(addedPassengers, "fullName", bookingRecord.getPassenger());

		dataset.put("passengers", passengerChoices);
		dataset.put("locatorCode", bookingRecord.getBooking().getLocatorCode());

		super.getResponse().addData(dataset);

	}

}
