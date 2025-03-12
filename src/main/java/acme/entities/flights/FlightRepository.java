
package acme.entities.flights;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface FlightRepository extends AbstractRepository {

	@Query("select min(l.scheduledDeparture) from Leg l where l.flight.id = :flightId")
	Date findScheduledDeparture(int flightId);

	@Query("select max(l.scheduledArrival) from Leg l where l.flight.id = :flightId")
	Date findScheduledArrival(int flightId);

	@Query("select l.destinationAirport.city from Leg l where l.flight.id = :flightId and l.scheduledDeparture = :departure")
	String findOriginCity(int flightId, Date departure);

	@Query("select l.originAirport.city from Leg l where l.flight.id = :flightId and l.scheduledArrival = :arrival")
	String findDestinationCity(int flightId, Date arrival);

	@Query("select count(l) from Leg l where l.flight.id = :flightId")
	int countLayovers(int flightId);

	@Query("select l from Leg l where l.flight.id = :flightId and l.draftMode=false")
	Leg publishedLegs(int flightId);
}
