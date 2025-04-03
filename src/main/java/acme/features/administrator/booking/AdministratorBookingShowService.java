
package acme.features.administrator.booking;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.passenger.Passenger;

@GuiService
public class AdministratorBookingShowService extends AbstractGuiService<Administrator, Booking> {

	@Autowired
	private AdministratorBookingRepository repository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		Booking booking = this.repository.findBookingById(id);
		super.getResponse().setAuthorised(booking != null && !booking.isDraftMode());
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Booking booking = this.repository.findBookingById(id);
		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		Collection<Passenger> passengers;

		// Obtener pasajeros de la reserva
		passengers = this.repository.findPassengersByBookingId(booking.getId());

		// Convertir la lista de pasajeros 
		List<String> passengerNames = passengers.stream().map(Passenger::getFullName).collect(Collectors.toList());

		// Asignar datos al dataset
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastCardNibble", "draftMode");
		dataset.put("bookingCost", booking.getCost());
		dataset.put("passengers", String.join(", ", passengerNames));

		super.getResponse().addData(dataset);
	}
}
