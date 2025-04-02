
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.features.customer.booking.CustomerBookingRepository;
import acme.realms.customer.Customer;

@GuiService
@Service
public class CustomerPassengerListService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerBookingRepository	bookingRepository;

	@Autowired
	private CustomerPassengerRepository	passengerRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

	}

	@Override
	public void unbind(final Passenger passenger) {

	}
}
