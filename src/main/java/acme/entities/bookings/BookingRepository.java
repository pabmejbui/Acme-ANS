
package acme.entities.bookings;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface BookingRepository extends AbstractRepository {

	@Query("SELECT b.locatorCode FROM Booking b")
	List<String> findAllLocatorCodes();

	@Query("select count(br.passenger) from BookingRecord br where br.booking.locatorCode = :locatorCode")
	Integer countPassengersByLocatorCode(String locatorCode);

}
