
package acme.features.any.weather;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flights.Flight;
import acme.entities.weather.WeatherCondition;

@Repository
public interface AnyWeatherRepository extends AbstractRepository {

	// Listar todas las condiciones meteorológicas ordenadas por fecha (para listado y detalles)
	@Query("SELECT wc FROM WeatherCondition wc ORDER BY wc.reportTime DESC")
	List<WeatherCondition> findAllWeatherConditions();

	// Obtener una condición meteorológica específica por su id (para mostrar detalle)
	@Query("SELECT wc FROM WeatherCondition wc WHERE wc.id = :id")
	WeatherCondition findWeatherConditionById(int id);

	// Listar vuelos del último mes con condiciones meteorológicas consideradas malas
	@Query("SELECT DISTINCT l.flight FROM Leg l, WeatherCondition wc " + "WHERE wc.city = l.originAirport.city AND wc.country = l.originAirport.country " + "AND ABS(TIMESTAMPDIFF(MINUTE, wc.reportTime, l.scheduledDeparture)) <= :maxDiffMinutes "
		+ "AND wc.reportTime BETWEEN :start AND :end " + "AND (wc.windSpeed > 30 OR " + "LOWER(wc.description) LIKE '%storm%' OR " + "LOWER(wc.description) LIKE '%rain%' OR " + "LOWER(wc.description) LIKE '%snow%' OR "
		+ "LOWER(wc.description) LIKE '%extreme%')")
	List<Flight> findFlightsWithBadWeather(Date start, Date end, int maxDiffMinutes);

}
