
package acme.features.any.weather;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.weather.WeatherCondition;

@Repository
public interface AnyWeatherRepository extends AbstractRepository {

	@Query("select w from WeatherCondition w order by w.reportTime desc")
	Collection<WeatherCondition> findAllWeatherConditions();

	@Query("select w from WeatherCondition w where w.id = :id")
	WeatherCondition findOneWeatherConditionById(int id);

}
