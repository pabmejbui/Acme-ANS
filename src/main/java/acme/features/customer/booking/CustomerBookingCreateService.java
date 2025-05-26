
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.basis.AbstractRealm;
import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.TravelClass;
import acme.entities.flights.Flight;
import acme.realms.customer.Customer;

@GuiService
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		boolean status = true;

		if (super.getRequest().hasData("id")) {
			Integer flightId = super.getRequest().getData("flight", Integer.class);
			if (flightId == null || flightId != 0) {
				Flight flight = this.repository.findFlightById(flightId);
				status = flight != null && !flight.isDraftMode();
			}
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking = new Booking();

		AbstractRealm principal = super.getRequest().getPrincipal().getActiveRealm();
		Integer customerId = principal.getId();
		Customer customer = this.repository.findCustomerById(customerId);

		booking.setCustomer(customer);
		booking.setPurchaseMoment(MomentHelper.getCurrentMoment());
		booking.setDraftMode(true);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "travelClass", "lastCardNibble", "flight");
	}

	@Override
	public void validate(final Booking booking) {
		;
	}

	@Override
	public void perform(final Booking booking) {
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
