
package acme.entities.claims;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.trackingLogs.TrackingLog;

@Repository
public interface ClaimRepository extends AbstractRepository {

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId ORDER BY t.lastUpdateMoment DESC")
	Optional<TrackingLog> findLastTrackingLogByClaimId(@Param("claimId") int claimId);

}
