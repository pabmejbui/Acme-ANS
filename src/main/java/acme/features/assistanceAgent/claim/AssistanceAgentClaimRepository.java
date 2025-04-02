
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.flights.Leg;
import acme.entities.trackingLogs.TrackingLog;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("SELECT c FROM Claim c")
	Collection<Claim> findAllClaims();

	@Query("SELECT c FROM Claim c WHERE c.assistanceAgent.id = :assistanceAgentId")
	Collection<Claim> findAllClaimsByAssistanceAgentId(int assistanceAgentId);

	@Query("SELECT c FROM Claim c WHERE c.id = :claimId")
	Claim findClaimById(int claimId);

	@Query("SELECT DISTINCT c FROM Claim c JOIN TrackingLog t ON t.claim.id = c.id WHERE t.lastUpdateMoment = (SELECT MAX(t2.lastUpdateMoment) FROM TrackingLog t2 WHERE t2.claim = c) AND (t.status = 'PENDING' AND c.assistanceAgent.id = :agentId)")
	Collection<Claim> findAllPendingClaimsByAgentId(int agentId);

	@Query("SELECT l FROM Leg l")
	Collection<Leg> findAllLeg();

	//@Query("SELECT l FROM Leg l WHERE l.draftMode = true AND l.arrival < :maxArrivalDate")
	//Collection<Leg> findAllPublishedLegsBefore(Date maxArrivalDate);

	@Query("SELECT l FROM Leg l WHERE l.draftMode = true")
	Collection<Leg> findAllPublishedLegs();

	@Query("SELECT l FROM Leg l WHERE l.id = :id")
	Leg findLegById(int id);

	@Query("SELECT tl FROM TrackingLog tl WHERE tl.claim.id = :claimId")
	Collection<TrackingLog> findTrackingLogsByClaimId(int claimId);

}
