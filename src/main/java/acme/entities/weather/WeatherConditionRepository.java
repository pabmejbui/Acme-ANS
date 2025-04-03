
package acme.entities.weather;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface WeatherConditionRepository extends AbstractRepository {

	@Query("select count(wc) > 0 from WeatherCondition wc where wc.city = :city and wc.country = :country and function('DATE_FORMAT', wc.reportTime, '%Y-%m-%d %H:%i') = function('DATE_FORMAT', :reportTime, '%Y-%m-%d %H:%i')")
	boolean existsDuplicateCondition(String city, String country, Date reportTime);

}
