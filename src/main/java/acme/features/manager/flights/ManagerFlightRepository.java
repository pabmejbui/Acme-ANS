
package acme.features.manager.flights;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flights.Flight;
import acme.realms.Manager;

@Repository
public interface ManagerFlightRepository extends AbstractRepository {

	@Query("SELECT f FROM Flight f WHERE f.manager.id = :managerId")
	List<Flight> findFlightsByManagerId(@Param("managerId") int managerId);

	@Query("SELECT f FROM Flight f WHERE f.id = :id")
	Flight findFlightById(@Param("id") int id);

	@Query("SELECT COUNT(l) FROM Leg l WHERE l.flight.id = :flightId")
	int countLegsByFlightId(@Param("flightId") int flightId);

	@Query("SELECT m FROM Manager m WHERE m.id = :id")
	Manager findManagerById(@Param("id") int id);

	@Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Flight f WHERE f.tag = :tag")
	boolean existsFlightByTag(@Param("tag") String tag);

	@Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Flight f WHERE f.tag = :tag AND f.id != :id")
	boolean existsFlightByTagExceptId(@Param("tag") String tag, @Param("id") int id);

	@Query("SELECT f FROM Flight f WHERE f.id = :id AND f.manager.id = :managerId")
	Flight findFlightByIdAndManagerId(@Param("id") int id, @Param("managerId") int managerId);

	@Query("SELECT COUNT(l) FROM Leg l WHERE l.flight.id = :flightId AND (l.status = 'LANDED' OR l.status = 'CANCELLED')")
	int countLandedOrCancelledLegsByFlightId(@Param("flightId") int flightId);

}
