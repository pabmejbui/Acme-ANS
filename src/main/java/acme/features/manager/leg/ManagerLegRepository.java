
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.flights.Leg;

public interface ManagerLegRepository extends AbstractRepository {

	@Query("select l from Leg l where l.flight.manager.id = :managerId")
	Collection<Leg> findLegsByManagerId(int managerId);

	@Query("select l from Leg l where l.id = :id")
	Leg findLegById(int id);
}
