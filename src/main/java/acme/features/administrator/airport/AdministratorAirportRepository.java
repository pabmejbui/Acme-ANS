
package acme.features.administrator.airport;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airports.Airport;

@Repository
public interface AdministratorAirportRepository extends AbstractRepository {

	@Query("SELECT a FROM Airport a")
	Collection<Airport> findAllAirports();

	@Query("SELECT a FROM Airport a WHERE a.id = :id")
	Airport findAirportById(int id);
}
