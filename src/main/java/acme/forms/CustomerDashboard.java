
package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerDashboard extends AbstractForm {

	// Serialisation identifier----------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes-------------------------------------------------

	//The last five destinations.
	private List<String>		lastFiveDestinations;

	//The money spent in bookings during the last year.
	double						totalMoneySpentBookingLastYear;

	//Their number of bookings grouped by travel class. 
	int							economyBookings;
	int							businessBookings;

	//Count, average, minimum, maximum, and standard deviation of the cost of their bookings in the last five years.
	int							totalBookings;
	double						avgBookingCost;
	double						minBookingCost;
	double						maxBookingCost;
	double						stdDevBookingCost;

	//Count, average, minimum, maximum, and standard deviation of the number of passengers in their bookings. 
	int							totalPassengers;
	double						avgPassengersPerBooking;
	double						minPassengersPerBooking;
	double						maxPassengersPerBooking;
	double						stdDevPassengersPerBooking;
}
