
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.flights.Flight;
import acme.entities.passenger.Passenger;

@Repository
public interface CustomerBookingRepository extends AbstractRepository {

	@Query("SELECT b FROM Booking b WHERE b.customer.id = :customerId")
	Collection<Booking> findBookingsByCustomerId(int customerId);

	@Query("SELECT b FROM Booking b where b.id = :bookingId")
	Booking findBookingById(int bookingId);

	@Query("SELECT p FROM Passenger p")
	Collection<Passenger> findAllPassengers();

	@Query("SELECT br.passenger FROM BookingRecord br WHERE br.booking.customer.id = :customerId")
	Collection<Passenger> findPassengersByCustomerId(@Param("customerId") int customerId);

	@Query("SELECT b FROM Booking b WHERE b.locatorCode = :locatorCode")
	Booking findBookingByLocatorCode(String locatorCode);

	@Query("SELECT br.passenger FROM BookingRecord br WHERE br.booking.id = :bookingId")
	Collection<Passenger> findPassengersByBookingId(@Param("bookingId") int bookingId);

	@Query("SELECT br FROM BookingRecord br WHERE br.booking.id = :bookingId")
	Collection<BookingRecord> findAllBookingRecordsByBookingId(int bookingId);

	@Query("SELECT f FROM Flight f WHERE f.draftMode = false")
	Collection<Flight> findAllFlightsDraftModeFalse();

	@Modifying
	@Query("DELETE FROM BookingRecord br WHERE br.passenger.id = :passengerId")
	void deleteBookingRecordsByPassengerId(int passengerId);
}
