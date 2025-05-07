
package acme.features.customer.recommendationDashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.recommendation.Recommendation;

@Repository
public interface CustomerRecommendationDashboardRepository extends AbstractRepository {

	@Query("select r from Recommendation r where r.city=:city")
	Collection<Recommendation> getRecommendationsOf(String city);

}
