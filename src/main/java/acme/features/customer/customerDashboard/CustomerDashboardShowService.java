
package acme.features.customer.customerDashboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.configuration.SystemConfiguration;
import acme.forms.CustomerDashboard;
import acme.realms.customer.Customer;

@GuiService
public class CustomerDashboardShowService extends AbstractGuiService<Customer, CustomerDashboard> {

	@Autowired
	private CustomerDashboardRepository repository;


	@Override
	public void authorise() {
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Customer customer = this.repository.findCustomerById(customerId);
		boolean status = customer != null && super.getRequest().getPrincipal().hasRealm(customer);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		// Obtener las últimas 5 destinaciones
		List<String> lastFiveDestinations = this.repository.findLastFiveDestinations(customerId);

		// Obtener la configuración del sistema
		SystemConfiguration config = this.repository.getSystemConfiguration();
		String currency = config.getCurrency();

		// Obtener información de los vuelos y los pasajeros
		List<Object[]> flightPriceAndPassengerCount = this.repository.findFlightPriceAndPassengerCount(customerId);

		// Calcular el total de dinero gastado el último año
		Double totalMoneySpentBookingLastYear = 0.0;
		for (Object[] result : flightPriceAndPassengerCount)
			if (result != null && result.length >= 2) {
				Double flightPrice = (Double) result[0];
				Long passengerCount = (Long) result[1];
				totalMoneySpentBookingLastYear += flightPrice * passengerCount;
			}

		// Convertir el total gastado en un objeto Money
		Money totalMoneySpentBookingLastYearObj = new Money();
		totalMoneySpentBookingLastYearObj.setCurrency(currency);
		totalMoneySpentBookingLastYearObj.setAmount(totalMoneySpentBookingLastYear);

		// Número de reservas por clase de viaje
		List<Object[]> bookingsByClass = this.repository.findBookingsGroupedByTravelClass(customerId);
		int economyBookings = 0;
		int businessBookings = 0;

		for (Object[] booking : bookingsByClass) {
			String travelClass = (String) booking[0];
			int count = ((Long) booking[1]).intValue();
			if ("economy".equalsIgnoreCase(travelClass))
				economyBookings = count;
			else if ("business".equalsIgnoreCase(travelClass))
				businessBookings = count;
		}
		int totalBookings = economyBookings + businessBookings;

		// Estadísticas de costos de las reservas
		Object[] bookingCostStats = this.repository.findBookingCostStatistics(customerId);
		Double avgBookingCostRaw = bookingCostStats != null && bookingCostStats.length > 1 ? (Double) bookingCostStats[1] : 0.0;
		Double minBookingCostRaw = bookingCostStats != null && bookingCostStats.length > 2 ? (Double) bookingCostStats[2] : 0.0;
		Double maxBookingCostRaw = bookingCostStats != null && bookingCostStats.length > 3 ? (Double) bookingCostStats[3] : 0.0;
		Double stdDevBookingCostRaw = bookingCostStats != null && bookingCostStats.length > 4 ? (Double) bookingCostStats[4] : 0.0;

		// Estadísticas de pasajeros
		List<Object[]> passengerStats = this.repository.findPassengerStatistics(customerId);
		Double avgPassengersPerBookingRaw = 0.0;
		Double minPassengersPerBookingRaw = 0.0;
		Double maxPassengersPerBookingRaw = 0.0;
		Double stdDevPassengersPerBookingRaw = 0.0;

		if (passengerStats != null && !passengerStats.isEmpty()) {
			Object[] stats = passengerStats.get(0);  // Accediendo a la primera entrada de la lista
			avgPassengersPerBookingRaw = stats != null ? (Double) stats[1] : 0.0;
			minPassengersPerBookingRaw = stats != null ? (Double) stats[2] : 0.0;
			maxPassengersPerBookingRaw = stats != null ? (Double) stats[3] : 0.0;
			stdDevPassengersPerBookingRaw = stats != null ? (Double) stats[4] : 0.0;
		}

		// Crear objetos Money para los costos
		Money avgBookingCost = new Money();
		avgBookingCost.setCurrency(currency);
		avgBookingCost.setAmount(avgBookingCostRaw);

		Money minBookingCost = new Money();
		minBookingCost.setCurrency(currency);
		minBookingCost.setAmount(minBookingCostRaw);

		Money maxBookingCost = new Money();
		maxBookingCost.setCurrency(currency);
		maxBookingCost.setAmount(maxBookingCostRaw);

		Money stdDevBookingCost = new Money();
		stdDevBookingCost.setCurrency(currency);
		stdDevBookingCost.setAmount(stdDevBookingCostRaw);

		// Crear el dashboard con la información recopilada
		CustomerDashboard dashboard = new CustomerDashboard();
		dashboard.setLastFiveDestinations(lastFiveDestinations);
		dashboard.setTotalMoneySpentBookingLastYear(totalMoneySpentBookingLastYearObj);
		dashboard.setEconomyBookings(economyBookings);
		dashboard.setBusinessBookings(businessBookings);
		dashboard.setTotalBookings(totalBookings);
		dashboard.setAvgBookingCost(avgBookingCost);
		dashboard.setMinBookingCost(minBookingCost);
		dashboard.setMaxBookingCost(maxBookingCost);
		dashboard.setStdDevBookingCost(stdDevBookingCost);

		dashboard.setAvgPassengersPerBooking(avgPassengersPerBookingRaw);
		dashboard.setMinPassengersPerBooking(minPassengersPerBookingRaw);
		dashboard.setMaxPassengersPerBooking(maxPassengersPerBookingRaw);
		dashboard.setStdDevPassengersPerBooking(stdDevPassengersPerBookingRaw);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final CustomerDashboard object) {
		Dataset dataset = super.unbindObject(object, //
			"lastFiveDestinations", "totalMoneySpentBookingLastYear", //
			"economyBookings", "businessBookings", "totalBookings", //
			"avgBookingCost", "minBookingCost", "maxBookingCost", //
			"stdDevBookingCost", "avgPassengersPerBooking", //
			"minPassengersPerBooking", "maxPassengersPerBooking", "stdDevPassengersPerBooking");

		super.getResponse().addData(dataset);
	}
}
