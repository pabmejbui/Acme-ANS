
package acme.features.customer.customerDashboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.bookings.TravelClass;
import acme.forms.CustomerDashboard;
import acme.realms.customer.Customer;

@GuiService
public class CustomerDashboardShowService extends AbstractGuiService<Customer, CustomerDashboard> {

	// Internal state ----------------------------

	@Autowired
	private CustomerDashboardRepository repository;

	// AbstractGuiService interface --------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		super.getResponse().setAuthorised(status);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void load() {
		Integer customerId = this.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<Booking> bookings = this.repository.findAllBookingsOf(customerId);
		Collection<BookingRecord> bookingPassengers = this.repository.findAllBookingPassengerOf(customerId);
		List<String> currencies = bookings.stream().map(b -> b.getCost().getCurrency()).distinct().toList();

		CustomerDashboard dashboard = new CustomerDashboard();

		dashboard.setLastFiveDestinations(List.of());
		dashboard.setSpentMoneyLastYear(new LinkedList<>());
		dashboard.setEconomyBookings(0);
		dashboard.setBusinessBookings(0);
		dashboard.setBookingCountCost(new LinkedList<>());
		dashboard.setBookingAverageCost(new LinkedList<>());
		dashboard.setBookingMinimumCost(new LinkedList<>());
		dashboard.setBookingMaximumCost(new LinkedList<>());
		dashboard.setBookingDeviationCost(new LinkedList<>());
		dashboard.setBookingCountPassengers(0);
		dashboard.setBookingAveragePassengers(Double.NaN);
		dashboard.setBookingMinimumPassengers(0);
		dashboard.setBookingMaximumPassengers(0);
		dashboard.setBookingDeviationPassengers(Double.NaN);

		if (!bookings.isEmpty() && !bookingPassengers.isEmpty()) {

			/* LAST FIVE DESTINATIONS */
			Integer thisYear = MomentHelper.getCurrentMoment().getYear();
			List<Booking> lastFiveYearsBookings = bookings.stream().filter(booking -> booking.getPurchaseMoment().getYear() > thisYear - 5).toList();

			List<String> last5destinations = bookings.stream().sorted(Comparator.comparing(Booking::getPurchaseMoment).reversed()).map(b -> b.getFlight().getDestinationCity()).distinct().limit(5).toList();
			dashboard.setLastFiveDestinations(last5destinations);

			/* SPENT MONEY LAST YEAR */
			for (String currency : currencies) {
				Double totalMoney = bookings.stream().filter(booking -> booking.getPurchaseMoment().getYear() > thisYear - 1).filter(booking -> booking.getCost().getCurrency().equals(currency)).map(Booking::getCost).map(Money::getAmount).reduce(0.0,
					Double::sum);

				Money spentMoney = new Money();
				spentMoney.setAmount(totalMoney);
				spentMoney.setCurrency(currency);

				List<Money> spentMoneys = new ArrayList<>(dashboard.getSpentMoneyLastYear());
				spentMoneys.add(spentMoney);
				dashboard.setSpentMoneyLastYear(spentMoneys);
			}

			/* ECONOMIC CLASS BOOKINGS */
			Integer economyBookings = (int) bookings.stream().filter(b -> b.getTravelClass().equals(TravelClass.ECONOMY)).count();
			dashboard.setEconomyBookings(economyBookings);

			/* BUSINESS CLASS BOOKINGS */
			Integer businessBookings = (int) bookings.stream().filter(b -> b.getTravelClass().equals(TravelClass.BUSINESS)).count();
			dashboard.setBusinessBookings(businessBookings);

			/* TOTAL COST OF BOOKINGS */
			for (String currency : currencies) {
				Money bookingTotalCost = new Money();
				bookingTotalCost.setAmount(lastFiveYearsBookings.stream().filter(booking -> booking.getCost().getCurrency().equals(currency)).map(Booking::getCost).map(Money::getAmount).reduce(0.0, Double::sum));
				bookingTotalCost.setCurrency(currency);

				List<Money> bookingTotalCosts = new ArrayList<>(dashboard.getBookingCountCost());
				bookingTotalCosts.add(bookingTotalCost);
				dashboard.setBookingCountCost(bookingTotalCosts);
			}

			/* AVERAGE COST OF BOOKINGS */
			for (String currency : currencies) {
				Money bookingAverageCost = new Money();
				Money bookingTotalCost = dashboard.getBookingCountCost().stream().filter(b -> b.getCurrency().equals(currency)).findFirst().get();
				Long total5YearsBookingCurrency = lastFiveYearsBookings.stream().filter(b -> b.getCost().getCurrency().equals(currency)).count();

				bookingAverageCost.setAmount(Double.NaN);
				if (total5YearsBookingCurrency > 0)
					bookingAverageCost.setAmount(bookingTotalCost.getAmount() / total5YearsBookingCurrency);

				bookingAverageCost.setCurrency(currency);

				List<Money> bookingAverageCosts = new ArrayList<>(dashboard.getBookingAverageCost());
				bookingAverageCosts.add(bookingAverageCost);
				dashboard.setBookingAverageCost(bookingAverageCosts);
			}

			/* MINIMUN COST OF BOOKINGS */
			for (String currency : currencies) {
				Money bookingMinimumCost = new Money();
				bookingMinimumCost.setAmount(lastFiveYearsBookings.stream().map(Booking::getCost).filter(p -> p.getCurrency().equals(currency)).map(Money::getAmount).min(Double::compare).orElse(0.0));
				bookingMinimumCost.setCurrency(currency);

				List<Money> bookingMinimumCosts = new ArrayList<>(dashboard.getBookingMinimumCost());
				bookingMinimumCosts.add(bookingMinimumCost);
				dashboard.setBookingMinimumCost(bookingMinimumCosts);
			}

			/* MAXIMUM COST OF BOOKINGS */
			for (String currency : currencies) {
				Money bookingMaximumCost = new Money();
				bookingMaximumCost.setAmount(lastFiveYearsBookings.stream().map(Booking::getCost).filter(p -> p.getCurrency().equals(currency)).map(Money::getAmount).max(Double::compare).orElse(0.0));
				bookingMaximumCost.setCurrency(currency);

				List<Money> bookingMaximumCosts = new ArrayList<>(dashboard.getBookingMaximumCost());
				bookingMaximumCosts.add(bookingMaximumCost);
				dashboard.setBookingMaximumCost(bookingMaximumCosts);
			}

			/* MAXIMUM DEVIATION OF BOOKING COST */
			for (String currency : currencies) {
				Money bookingDeviationCost = new Money();
				Money bookingAverageCost = dashboard.getBookingAverageCost().stream().filter(p -> p.getCurrency().equals(currency)).findFirst().get();
				Long total5YearsBookingCurrency = lastFiveYearsBookings.stream().filter(b -> b.getCost().getCurrency().equals(currency)).count();

				bookingDeviationCost.setAmount(Double.NaN);
				if (total5YearsBookingCurrency > 0) {
					Double varianza = lastFiveYearsBookings.stream().map(Booking::getCost).filter(p -> p.getCurrency().equals(currency)).map(Money::getAmount).map(price -> Math.pow(price - bookingAverageCost.getAmount(), 2)).reduce(0.0, Double::sum)
						/ total5YearsBookingCurrency;
					Double deviation = Math.sqrt(varianza);
					bookingDeviationCost.setAmount(deviation);
				}
				bookingDeviationCost.setCurrency(currency);

				List<Money> bookingDeviationCosts = new ArrayList<>(dashboard.getBookingDeviationCost());
				bookingDeviationCosts.add(bookingDeviationCost);
				dashboard.setBookingDeviationCost(bookingDeviationCosts);
			}

			/* TOTAL NUMBER OF PASSENGERS */
			Integer passengerCount = (int) bookingPassengers.stream().map(BookingRecord::getPassenger).count();
			dashboard.setBookingCountPassengers(passengerCount);

			/* AVERAGE NUMBER OF PASSENGERS PER BOOKING */
			Integer numBookings = bookings.size();
			Double passengerAverage = (double) passengerCount / numBookings;
			dashboard.setBookingAveragePassengers(passengerAverage);

			Map<Booking, Long> numberOfPassengerByBooking = bookingPassengers.stream().collect(Collectors.groupingBy(BookingRecord::getBooking, Collectors.counting()));

			/* MINIMUM NUMBER OF PASSENGERS PER BOOKING */
			Integer minimumPassengers = numberOfPassengerByBooking.isEmpty() ? 0 : Collections.min(numberOfPassengerByBooking.values()).intValue();
			dashboard.setBookingMinimumPassengers(minimumPassengers);

			/* MAXIMUM NUMBER OF PASSENGERS PER BOOKING */
			Integer maximumPassengers = numberOfPassengerByBooking.isEmpty() ? 0 : Collections.max(numberOfPassengerByBooking.values()).intValue();
			dashboard.setBookingMaximumPassengers(maximumPassengers);

			/* STANDARD DEVIATION OF PASSENGERS PER BOOKING */
			Double variancePassengers = numberOfPassengerByBooking.values().stream().mapToDouble(count -> Math.pow(count - passengerAverage, 2)).sum() / (numBookings - 1);
			Double standardDeviationPassengers = Math.sqrt(variancePassengers);
			dashboard.setBookingDeviationPassengers(standardDeviationPassengers);
		}

		super.getBuffer().addData(dashboard);

	}

	@Override
	public void unbind(final CustomerDashboard object) {
		Dataset dataset = super.unbindObject(object, "lastFiveDestinations", "spentMoneyLastYear", "economyBookings", "businessBookings", "bookingCountCost", "bookingAverageCost", "bookingMinimumCost", "bookingMaximumCost", "bookingDeviationCost",
			"bookingCountPassengers", "bookingAveragePassengers", "bookingMinimumPassengers", "bookingMaximumPassengers", "bookingDeviationPassengers");

		super.getResponse().addData(dataset);
	}
}
