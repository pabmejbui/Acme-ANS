
package acme.features.customer.bookingRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.bookings.BookingRecord;
import acme.realms.customer.Customer;

@GuiController
public class CustomerBookingRecordController extends AbstractGuiController<Customer, BookingRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordCreateService	customerBookingPassengerCreateService;

	@Autowired
	private CustomerBookingRecordDeleteService	customerBookingPassengerDeleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.customerBookingPassengerCreateService);
		super.addBasicCommand("delete", this.customerBookingPassengerDeleteService);

	}
}
