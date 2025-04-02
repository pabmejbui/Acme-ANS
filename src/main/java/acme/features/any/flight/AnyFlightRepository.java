
package acme.features.any.flight;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flights.Flight;
import acme.entities.flights.Leg;

@Repository
public interface AnyFlightRepository extends AbstractRepository {

	@Query("select f from Flight f where f.draftMode = false")
	Collection<Flight> findManyPublished();

	@Query("select f from Flight f where f.id = :id")
	Flight findOneFlightById(int id);

	@Query("select l from Leg l where l.flight.id = :id order by l.scheduledDeparture asc")
	Collection<Leg> findLegsByFlightId(int id);

}
