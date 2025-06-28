
package acme.features.customer.booking;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.TravelClass;
import acme.entities.flights.Flight;
import acme.entities.flights.Leg;
import acme.realms.customer.Customer;

@GuiService
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		boolean status = true;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking = new Booking();
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		booking.setFlight(null);
		booking.setLocatorCode("");
		booking.setPurchaseMoment(MomentHelper.getCurrentMoment());
		booking.setTravelClass(TravelClass.ECONOMY);
		booking.setLastCardNibble("");
		booking.setDraftMode(true);
		booking.setCustomer(customer);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {

		super.bindObject(booking, "locatorCode", "travelClass", "lastCardNibble", "flight");
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		booking.setCustomer(customer);
	}

	@Override
	public void validate(final Booking booking) {
		if (booking.getFlight() != null) {
			Flight flight = booking.getFlight();
			super.state(!flight.isDraftMode(), "flight", "customer.booking.form.error.flight-draft");
			Date currentDate = MomentHelper.getCurrentMoment();
			Collection<Flight> availableFlights = this.repository.findAvailableFlights(currentDate);

			super.state(availableFlights.contains(flight), "flight", "customer.booking.form.error.flight-not-available");
		}
	}

	@Override
	public void perform(final Booking booking) {
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices travelClasses;
		SelectChoices flightChoices = new SelectChoices();
		;
		Collection<Flight> flights;

		travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		Date currentDate = MomentHelper.getCurrentMoment();
		flights = this.repository.findAvailableFlights(currentDate);
		flightChoices.add("0", "----", booking.getFlight() == null);

		for (Flight flight : flights) {
			String tag = flight.getTag();
			Date scheduledDeparture = this.getScheduledDeparture(flight);
			String destinationCity = this.getDestination(flight);

			String departureDateStr = "";
			if (scheduledDeparture != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				departureDateStr = sdf.format(scheduledDeparture);
			}

			String destination = destinationCity != null ? destinationCity : "no destination";
			String label = String.format("%s --- Dest: %s ---  %s", tag, destination, departureDateStr);

			flightChoices.add(Integer.toString(flight.getId()), label, flight.equals(booking.getFlight()));
		}

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastCardNibble", "flight", "draftMode");
		dataset.put("bookingCost", booking.getCost());
		dataset.put("flightChoices", flightChoices);
		dataset.put("travelClasses", travelClasses);

		super.getResponse().addData(dataset);
	}

	private Date getScheduledDeparture(final Flight flight) {
		List<Leg> legs = this.repository.findLegsByFlightByDeparture(flight.getId());

		if (legs == null || legs.isEmpty())
			return null;

		Leg l = legs.get(0);
		return l.getScheduledDeparture();
	}

	private String getDestination(final Flight flight) {
		List<Leg> legs = this.repository.findLegsByFlightByArrival(flight.getId());

		if (legs == null || legs.isEmpty())
			return "No legs available";

		Leg lastLeg = legs.get(legs.size() - 1);

		if (lastLeg.getDestinationAirport() != null)
			return lastLeg.getDestinationAirport().getCity();
		else
			return "No detination airport defined";
	}
}
