
package acme.features.customer.booking;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.TravelClass;
import acme.entities.passenger.Passenger;
import acme.realms.customer.Customer;

@GuiService
public class CustomerBookingShowService extends AbstractGuiService<Customer, Booking> {

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
		status = super.getRequest().getPrincipal().hasRealm(customer) || booking != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int bookingId;
		Booking booking;

		// Obtener la reserva por ID
		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(bookingId);

		// Agregar la reserva al buffer
		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices travelClasses;
		Collection<Passenger> passengers;

		// Obtener pasajeros de la reserva
		passengers = this.repository.findPassengersByBooking(booking.getId());

		// Convertir la lista de pasajeros a una representación adecuada para la vista
		List<String> passengerNames = passengers.stream().map(Passenger::getFullName)  // Obtener nombres de los pasajeros
			.collect(Collectors.toList());

		// Opciones de TravelClass
		travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		// Asignar datos al dataset
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastCardNibble", "draftMode");
		dataset.put("travelClasses", travelClasses);
		dataset.put("passengers", String.join(", ", passengerNames)); // Convertir lista a string

		// Enviar datos al frontend
		super.getResponse().addData(dataset);
	}
}
