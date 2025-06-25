
package acme.features.administrator.recommendation;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airports.Airport;
import acme.entities.recomendations.Recommendation;

@Repository
public interface AdministratorRecommendationRepository extends AbstractRepository {

	// Query to find all airports in the system
	@Query("SELECT a FROM Airport a")
	List<Airport> findAllAirports();

	// Query to find all recommendations associated with a specific airport ID
	@Query("SELECT r FROM Recommendation r WHERE r.airport.id = :airportId")
	List<Recommendation> findByAirportId(int airportId);

	// Query to check if a recommendation with a specific name already exists for an airport
	@Query("SELECT COUNT(r) > 0 FROM Recommendation r WHERE r.name = :name AND r.airport.id = :airportId")
	boolean existsByNameAndAirport(String name, int airportId);

}
