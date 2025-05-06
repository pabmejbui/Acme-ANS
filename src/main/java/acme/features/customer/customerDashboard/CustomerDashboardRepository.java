
package acme.features.customer.customerDashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;

@Repository
public interface CustomerDashboardRepository extends AbstractRepository {

	@Query("SELECT b FROM Booking b WHERE b.customer.id =:customerId AND b.draftMode=false")
	Collection<Booking> findAllBookingsOf(int customerId);

	@Query("SELECT bp FROM BookingRecord bp WHERE bp.booking.customer.id =:customerId")
	Collection<BookingRecord> findAllBookingPassengerOf(int customerId);
}
