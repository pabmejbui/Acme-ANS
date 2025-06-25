
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.passenger.Passenger;

@Repository
public interface CustomerBookingRecordRepository extends AbstractRepository {

	@Query("SELECT b FROM Booking b WHERE b.id=:bookingId")
	Booking findBookingById(Integer bookingId);

	@Query("SELECT p FROM Passenger p WHERE p.id=:passengerId")
	Passenger findPassengerById(Integer passengerId);

	@Query("SELECT p FROM Passenger p WHERE p.customer.id=:customerId")
	Collection<Passenger> findAllPassengersByCustomerId(Integer customerId);

	@Query("SELECT br.passenger FROM BookingRecord br WHERE br.booking.id=:bookingId")
	Collection<Passenger> findAllPassengersByBookingId(Integer bookingId);

	@Query("SELECT br FROM BookingRecord br WHERE br.booking.id =:bookingId AND br.passenger.id =:passengerId")
	BookingRecord findBookingPassengerByBothIds(Integer bookingId, Integer passengerId);
}
