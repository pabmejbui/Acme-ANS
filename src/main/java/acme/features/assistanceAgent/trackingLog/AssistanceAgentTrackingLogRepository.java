
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.trackingLogs.TrackingLog;

@Repository
public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId")
	Collection<TrackingLog> findTrackingLogsByClaimId(int claimId);

	@Query("SELECT c FROM Claim c WHERE c.id = :id")
	Claim findClaimById(int id);

	@Query("SELECT t FROM TrackingLog t WHERE t.id = :id")
	TrackingLog findTrackingLogById(int id);

	Collection<TrackingLog> findLatestTrackingLogsByClaimId(int claimId);

}
