
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.trackingLogs.TrackingLog;

@Repository
public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	@Query("select t from TrackingLog t where t.id = :id")
	TrackingLog findTrackingLogById(int id);

	@Query("select t from TrackingLog t where t.claim.id = :claimId")
	Collection<TrackingLog> findTrackingLogsByClaimId(int claimId);

	@Query("select t from TrackingLog t where t.claim.assistanceAgent.id = :assistanceAgentId")
	Collection<TrackingLog> findTrackingLogsByAssistanceAgentId(int assistanceAgentId);

	@Query("select c.draftMode from Claim c where c.id = :claimId")
	boolean isClaimPublished(int claimId);

	@Query("select c from Claim c where c.id = :claimId")
	Claim findClaimById(int claimId);

	@Query("select t from TrackingLog t where t.claim.id = :claimId order by t.lastUpdateMoment desc")
	Collection<TrackingLog> findLatestTrackingLogsByClaimId(int claimId);

	@Modifying
	@Query("delete from TrackingLog t where t.id = :id")
	void deleteTrackingLogById(int id);
}
