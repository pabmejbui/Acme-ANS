
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airlines.Airline;
import acme.entities.airports.Airport;
import acme.entities.flights.Leg;

public interface ManagerLegRepository extends AbstractRepository {

	@Query("select l from Leg l where l.flight.manager.id = :managerId")
	Collection<Leg> findLegsByManagerId(int managerId);

	@Query("select l from Leg l where l.flight.id = :flightId and l.flight.manager.id = :managerId")
	Collection<Leg> findLegsByFlightIdAndManagerId(int flightId, int managerId);

	@Query("select l from Leg l where l.id = :id")
	Leg findLegById(int id);

	@Query("select a from Aircraft a")
	Collection<Aircraft> findAllAircrafts();

	@Query("select a from Airport a")
	Collection<Airport> findAllAirports();

	@Query("select a from Airline a")
	Collection<Airline> findAllAirlines();
}
