
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;

@Repository
public interface FlightCrewMemberActivityLogRepository extends AbstractRepository {

	@Query("select al from ActivityLog al where al.id = :logId")
	ActivityLog findActivityLogById(int logId);

	@Query("select fa from FlightAssignment fa where fa.id = :assignmentId")
	FlightAssignment findFlightAssignmentById(Integer assignmentId);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :id AND fa.leg.draftMode = false")
	Collection<FlightAssignment> findPublishedAssignmentsByFlightCrewMemberId(int id);

	@Query("select lo from ActivityLog lo where lo.flightAssignment.id = :masterId")
	Collection<ActivityLog> findActivityLogsByMasterId(int masterId);

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :memberId or fa.draftMode = false")
	Collection<FlightAssignment> findFlightAssignmentsByMemberIdOrPublished(int memberId);

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :memberId")
	Collection<FlightAssignment> findFlightAssignmentsByMemberId(int memberId);

	@Query("select lo from ActivityLog lo where lo.flightAssignment.id = :masterId and lo.draftMode = false")
	Collection<ActivityLog> findPublishedActivityLogsByMasterId(int masterId);
}
