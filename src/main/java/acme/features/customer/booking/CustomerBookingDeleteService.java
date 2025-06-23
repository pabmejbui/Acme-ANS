
package acme.features.customer.booking;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.TravelClass;
import acme.entities.flights.Flight;
import acme.entities.flights.Leg;
import acme.entities.passenger.Passenger;
import acme.realms.customer.Customer;

@GuiService
public class CustomerBookingDeleteService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Integer masterId;
		Booking booking;

		masterId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(masterId);
		status = booking != null && booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(booking.getCustomer());

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
		super.bindObject(booking);
	}

	@Override
	public void validate(final Booking booking) {

		int id = super.getRequest().getData("id", int.class);
		Collection<Passenger> passengers = this.repository.findPassengersByBookingId(id);

		boolean hasPublishedPassengers = passengers.stream().anyMatch(p -> !p.isDraftMode());

		// Si hay al menos un pasajero publicado, no permitir la eliminación
		super.state(!hasPublishedPassengers, "*", "customer.booking.delete.published-passengers");
	}

	@Override
	public void perform(final Booking booking) {
		Collection<Passenger> passengers = this.repository.findPassengersByBookingId(booking.getId());

		for (Passenger passenger : passengers)
			this.repository.deleteBookingRecordsByPassengerId(passenger.getId()); // Eliminar dependencias primero

		this.repository.deleteAll(passengers);
		this.repository.delete(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices travelClasses;
		travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastCardNibble", "flight", "draftMode");

		if (booking.getFlight() != null) {
			Flight flight = booking.getFlight();
			String tag = flight.getTag();

			Date scheduledDeparture = this.getScheduledDeparture(flight);
			String destinationCity = this.getDestination(flight);

			String depDateStr = "";
			if (scheduledDeparture != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				depDateStr = sdf.format(scheduledDeparture);
			}

			String destination = destinationCity != null ? destinationCity : "no destination";
			String flightData = String.format("%s --- Dest: %s ---  %s", tag, destination, depDateStr);

			dataset.put("flightData", flightData);
		} else
			dataset.put("flightData", "---");

		dataset.put("bookingCost", booking.getCost());
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
