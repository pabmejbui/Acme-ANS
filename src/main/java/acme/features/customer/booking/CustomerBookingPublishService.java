
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.bookings.TravelClass;
import acme.entities.flights.Flight;
import acme.realms.customer.Customer;

@GuiService
public class CustomerBookingPublishService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getMethod().equals("POST");

		try {

			Integer bookingId = super.getRequest().getData("id", Integer.class);
			Booking booking = this.repository.findBookingById(bookingId);

			status = status && booking != null;

			Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

			status = status && booking.getCustomer().getId() == customerId && booking.isDraftMode();

			Integer flightId = super.getRequest().getData("flight", Integer.class);
			if (flightId == null || flightId != 0) {
				Flight flight = this.repository.findFlightById(flightId);
				status = status && flight != null && !flight.isDraftMode();
			}

		} catch (Throwable E) {
			status = false;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking;
		Integer id;

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "travelClass", "lastCardNibble", "flight");
	}

	@Override
	public void validate(final Booking booking) {

		Collection<BookingRecord> bookingRecords = this.repository.findAllBookingRecordsByBookingId(booking.getId());
		boolean status = !bookingRecords.isEmpty();
		super.state(status, "*", "customer.validation.booking.form.error.noPassengers");

		status = bookingRecords.stream().filter(br -> br.getPassenger().isDraftMode()).findFirst().isEmpty();
		super.state(status, "*", "customer.booking.publish.published-passengers");

		status = !booking.getLastCardNibble().isBlank();
		super.state(status, "lastCardNibble", "customer.booking.publish.missing-card-nibble");

	}

	@Override
	public void perform(final Booking booking) {
		booking.setDraftMode(false);
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices travelClasses;
		SelectChoices flightChoices;
		Collection<Flight> flights;
		travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		flights = this.repository.findAllFlightsDraftModeFalse();
		flightChoices = SelectChoices.from(flights, "id", booking.getFlight());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastCardNibble", "flight", "draftMode");
		dataset.put("bookingCost", booking.getCost());
		dataset.put("flightChoices", flightChoices);
		dataset.put("travelClasses", travelClasses);
		super.getResponse().addData(dataset);
	}

}
