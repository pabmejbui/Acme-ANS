
package acme.features.customer.recomendation;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.recomendations.Recommendation;

@Repository
public interface CustomerRecommendationRepository extends AbstractRepository {

	@Query("""
		SELECT DISTINCT rec
		FROM Recommendation rec
		JOIN rec.airport apt
		WHERE apt.id IN (
		    SELECT l.destinationAirport.id
		    FROM Booking book
		    JOIN book.flight fl
		    JOIN Leg l ON l.flight.id = fl.id
		    WHERE book.customer.id = :customerId
		    AND book.draftMode = false
		    AND NOT EXISTS (
		        SELECT l1 FROM Leg l1
		        WHERE l1.flight.id = fl.id
		        AND l1.scheduledDeparture > l.scheduledDeparture
		    )
		)
		""")
	Collection<Recommendation> retrievePoisForCustomerArrivals(int customerId);

	@Query("SELECT rec FROM Recommendation rec WHERE rec.id = :entryId")
	Recommendation findPoiEntryById(int entryId);

	@Query("SELECT COUNT(rec) > 0 FROM Recommendation rec JOIN rec.airport apt " + "WHERE apt.id = :airportIdentifier AND apt.id IN (" + "    SELECT leg.destinationAirport.id " + "    FROM Booking book " + "    JOIN book.flight fl "
		+ "    JOIN Leg leg ON leg.flight.id = fl.id " + "    WHERE book.customer.id = :customerId " + "AND book.draftMode = false)")
	boolean verifiesArrivalAirportForClient(int airportIdentifier, int customerId);

}
