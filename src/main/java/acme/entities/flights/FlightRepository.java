
package acme.entities.flights;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface FlightRepository extends AbstractRepository {

	@Query("SELECT MIN(l.scheduledDeparture) FROM Leg l WHERE l.flight.id = :flightId")
	Date findScheduledDeparture(int flightId);

	@Query("SELECT MAX(l.scheduledArrival) FROM Leg l WHERE l.flight.id = :flightId")
	Date findScheduledArrival(int flightId);

	@Query("SELECT l.originAirport.city FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledDeparture ASC")
	String findOriginCity(int flightId);

	@Query("SELECT l.destinationAirport.city FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledArrival DESC")
	String findDestinationCity(int flightId);

	@Query("SELECT COUNT(l) - 1 FROM Leg l WHERE l.flight.id = :flightId")
	Integer countLayovers(int flightId);

	@Query("SELECT f FROM Flight f WHERE f.id = :flightId")
	Flight findOneFlightById(int flightId);

}
