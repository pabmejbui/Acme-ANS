
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;

@Repository
public interface CustomerBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.id = :id")
	Booking findBookingById(final int id);

	@Query("select b from Booking b where b.customer.id = :customerId")
	Collection<Booking> findBookingByCustomerId(final int customerId);

	@Query("select b from Booking b where b.customer.id = :customerId and b.draftMode = true")
	Collection<Booking> findDraftBookingsByCustomerId(final int customerId);

	@Query("select b from Booking b where b.customer.id = :customerId and b.draftMode = false")
	Collection<Booking> findPublishedBookingsByCustomerId(final int customerId);
}
