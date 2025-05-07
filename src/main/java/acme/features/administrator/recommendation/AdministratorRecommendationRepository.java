
package acme.features.administrator.recommendation;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flights.Flight;

@Repository
public interface AdministratorRecommendationRepository extends AbstractRepository {

	@Query("DELETE from Recommendation")
	@Modifying
	@Transactional
	void deleteAllRecommendations();

	@Query("SELECT f FROM Flight f")
	List<Flight> findAllFlights();
}
