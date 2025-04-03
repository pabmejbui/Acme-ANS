
package acme.features.customer.customerDashboard;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.configuration.SystemConfiguration;
import acme.entities.bookings.Booking;
import acme.realms.customer.Customer;

@Repository
public interface CustomerDashboardRepository extends AbstractRepository {

	@Query("select c from Customer c where c.id = :id")
	Customer findCustomerById(@Param("id") int id);

	@Query("select distinct l.destinationAirport.city from Booking b " + "join b.flight f " + "join Leg l on l.flight.id = f.id " + "where b.customer.id = :customerId " + "order by b.purchaseMoment desc")
	List<String> findLastFiveDestinations(@Param("customerId") int customerId);

	@Query("select b from Booking b where b.customer.id = :customerId " + "and b.purchaseMoment between CURRENT_DATE - 365 and CURRENT_DATE")
	List<Booking> findBookingsLastYear(@Param("customerId") int customerId);

	@Query("select b.travelClass, count(b) from Booking b " + "where b.customer.id = :customerId " + "group by b.travelClass")
	List<Object[]> findBookingsGroupedByTravelClass(@Param("customerId") int customerId);

	@Query("select count(b), avg(b.cost.amount), min(b.cost.amount), max(b.cost.amount), stddev(b.cost.amount) " + "from Booking b where b.customer.id = :customerId " + "and b.purchaseMoment between CURRENT_DATE - (5 * 365) and CURRENT_DATE")
	Object[] findBookingCostStatistics(@Param("customerId") int customerId);

	@Query("SELECT b.booking.id, p.id, COUNT(b) " + "FROM BookingRecord b " + "JOIN b.passenger p " + "WHERE b.booking.id = :bookingId " + "GROUP BY b.booking.id, p.id")
	List<Object[]> findPassengerStatistics(@Param("bookingId") int bookingId);

	@Query("select f.cost.amount, count(b) from Booking b " + "join b.flight f where b.customer.id = :customerId " + "and b.purchaseMoment between CURRENT_DATE - 365 and CURRENT_DATE " + "group by f.cost.amount")
	List<Object[]> findFlightPriceAndPassengerCount(@Param("customerId") int customerId);

	@Query("select count(br) from BookingRecord br where br.booking.customer.id = :customerId")
	Integer findTotalPassengers(@Param("customerId") int customerId);

	@Query("select sc from SystemConfiguration sc")
	SystemConfiguration getSystemConfiguration();
}
