
package acme.entities.weather;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface WeatherRepository extends AbstractRepository {

	@Query("SELECT w FROM WeatherCondition w WHERE w.city = :city AND w.country = :country ORDER BY w.reportTime DESC")
	WeatherCondition findRecentWeather(String city, String country);
}
