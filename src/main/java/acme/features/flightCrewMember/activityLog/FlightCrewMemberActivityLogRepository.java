
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;

@Repository
public interface FlightCrewMemberActivityLogRepository extends AbstractRepository {

	// List all the activity logs from a flight assignment Id
	@Query("SELECT a FROM ActivityLog a WHERE a.flightAssignment.id = :flightAssignmentId")
	Collection<ActivityLog> findActivityLogsByFlightAssignmentId(int flightAssignmentId);

	// Show an activity log by its id
	@Query("SELECT a FROM ActivityLog a WHERE a.id = :activityLogId")
	ActivityLog findActivityLogById(int activityLogId);

	@Query("SELECT a FROM FlightAssignment a WHERE a.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select a from ActivityLog a where a.flightAssignment.leg.flight.id=:flightId")
	Collection<ActivityLog> findActivityLogsByFlightId(int flightId);

	@Query("select a from ActivityLog a where a.flightAssignment.leg.id=:legId")
	Collection<ActivityLog> findActivityLogsByLegId(int legId);
}
