
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.passenger.Passenger;

public interface BookingRecordRepository extends AbstractRepository {

	@Query("Select br.passenger from BookingRecord br where br.booking.id =:bookingId")
	Collection<Passenger> findPassengenrsByBooking(int bookingId);

	@Query("Select br.booking from BookingRecord br where br.passenger.id =:id")
	Booking findBookingByPassengerId(int id);

	@Query("Select br from BookingRecord br where br.passenger.id =:id")
	BookingRecord findBookingRecord(int id);

	@Query("Select br from BookingRecord br where br.booking.id =:id")
	Collection<BookingRecord> findBookingRecordByBooking(int id);
}
