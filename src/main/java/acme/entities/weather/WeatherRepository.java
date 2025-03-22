
package acme.entities.weather;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface WeatherRepository extends AbstractRepository {

	@Query("SELECT wc FROM WeatherCondition wc WHERE wc.city = :city AND wc.country = :country AND wc.reportTime = :reportTime")
	WeatherCondition findWeatherByCityCountryAndTime(String city, String country, Date reportTime);
}
