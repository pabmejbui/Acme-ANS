
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flights.Leg;

@Repository
public interface FlightCrewMemberActivityLogRepository extends AbstractRepository {

	@Query("select al from ActivityLog al where al.flightAssignment.flightCrewMember.id = :memberId")
	Collection<ActivityLog> findAllLogsByFlightCrewMemberId(int memberId);

	@Query("select al from ActivityLog al where al.id = :logId")
	ActivityLog findActivityLogById(int logId);

	@Query("select fa from FlightAssignment fa")
	Collection<FlightAssignment> findAllAssignments();

	@Query("select fa from FlightAssignment fa where fa.id = :assignmentId")
	FlightAssignment findFlightAssignmentById(Integer assignmentId);

	@Query("select l from Leg l where l.id = :legId")
	Leg findLegById(int legId);
}
