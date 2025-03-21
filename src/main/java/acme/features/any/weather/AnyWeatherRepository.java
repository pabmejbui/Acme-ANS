
package acme.features.any.weather;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flights.Flight;
import acme.entities.weather.WeatherCondition;

@Repository
public interface AnyWeatherRepository extends AbstractRepository {

	/**
	 * Obtiene todas las condiciones meteorológicas almacenadas en la base de datos.
	 */
	@Query("SELECT w FROM WeatherCondition w")
	Collection<WeatherCondition> findAllWeatherConditions();

	/**
	 * Busca una condición meteorológica específica por su ID.
	 */
	@Query("SELECT w FROM WeatherCondition w WHERE w.id = :id")
	WeatherCondition findWeatherConditionById(int id);

	/**
	 * Busca vuelos que se realizaron en los últimos 30 días bajo malas condiciones meteorológicas.
	 * Se eliminó el uso de `scheduledDeparture` directamente en `Flight`, ya que es un campo derivado.
	 */
	@Query("""
		    SELECT DISTINCT f
		    FROM Flight f
		    WHERE EXISTS (
		        SELECT l FROM Leg l
		        WHERE l.flight = f
		        AND l.scheduledDeparture BETWEEN CURRENT_DATE - 30 AND CURRENT_DATE
		    )
		    AND EXISTS (
		        SELECT wc FROM WeatherCondition wc
		        WHERE wc.city = (
		            SELECT l.originAirport.city FROM Leg l
		            WHERE l.flight = f
		            AND l.scheduledDeparture = (SELECT MIN(l2.scheduledDeparture) FROM Leg l2 WHERE l2.flight = f)
		        )
		        AND wc.reportTime BETWEEN
		            FUNCTION('timestampadd', 'HOUR', -2, (SELECT MIN(l.scheduledDeparture) FROM Leg l WHERE l.flight = f))
		        AND FUNCTION('timestampadd', 'HOUR', 2, (SELECT MIN(l.scheduledDeparture) FROM Leg l WHERE l.flight = f))
		        AND wc.description IN ('Thunderstorm', 'Snow', 'Extreme', 'Tornado')
		    )
		""")
	Collection<Flight> findFlightsWithBadWeatherLastMonth();

}
