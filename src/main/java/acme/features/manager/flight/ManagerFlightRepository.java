
package acme.features.manager.flight;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.configuration.SystemConfiguration;
import acme.entities.flights.Flight;
import acme.entities.flights.Leg;

@Repository
public interface ManagerFlightRepository extends AbstractRepository {

	@Query("select count(l) from Leg l where l.flight.id = :id")
	int countLegsByFlightId(int id);

	@Query("select f from Flight f where f.id = :id")
	Flight findFlightById(int id);

	@Query("select f from Flight f")
	Collection<Flight> findAllFlights();

	@Query("select f from Flight f where f.manager.id = :id")
	Collection<Flight> findFlightsByManagerId(int id);

	@Query("select l from Leg l where l.id = :id")
	Leg findLegById(int id);

	@Query("select l from Leg l where l.flight.id = :flightId")
	Collection<Leg> findLegsByFlightId(int flightId);

	@Query("SELECT sc FROM SystemConfiguration sc")
	List<SystemConfiguration> findSystemConfiguration();
}
