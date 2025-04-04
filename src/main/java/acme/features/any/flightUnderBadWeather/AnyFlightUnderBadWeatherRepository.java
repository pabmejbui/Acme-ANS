
package acme.features.any.flightUnderBadWeather;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flights.Flight;

@Repository
public interface AnyFlightUnderBadWeatherRepository extends AbstractRepository {

	@Query("SELECT f FROM Flight f WHERE f.draftMode = false AND f.cost.amount IS NOT NULL AND f.cost.currency IS NOT NULL AND f.manager IS NOT NULL")
	Collection<Flight> findAllPublishedFlights();

	@Query("SELECT MIN(wc.reportTime) FROM WeatherCondition wc")
	Date findEarliestWeatherReport();
}
