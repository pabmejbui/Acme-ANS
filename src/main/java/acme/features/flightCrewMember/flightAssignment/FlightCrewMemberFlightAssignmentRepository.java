
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flights.Leg;
import acme.realms.flightCrewMembers.FlightCrewMember;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select fa from FlightAssignment fa")
	Collection<FlightAssignment> findAllFlightAssignments();

	@Query("select fa from FlightAssignment fa where fa.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select fa from FlightAssignment fa where fa.leg.scheduledArrival < :now and fa.flightCrewMember.id = :id")
	Collection<FlightAssignment> findCompletedFlightAssignmentsByMemberId(Date now, int id);

	@Query("select fa from FlightAssignment fa where fa.leg.scheduledDeparture > :now and fa.flightCrewMember.id = :id")
	Collection<FlightAssignment> findPlannedFlightAssignmentsByMemberId(Date now, int id);

	@Query("select l from Leg l")
	Collection<Leg> findAllLegs();

	@Query("select l from Leg l where l.id = :legId")
	Leg findLegById(int legId);

	@Query("select m from FlightCrewMember m")
	Collection<FlightCrewMember> findAllFlightCrewMembers();

	@Query("select m from FlightCrewMember m where m.id = :memberId")
	FlightCrewMember findFlightCrewMemberById(int memberId);

	@Query("select l from ActivityLog l where l.flightAssignment.id = :id")
	Collection<ActivityLog> findActivityLogsByAssignmentId(int id);

	@Query("select distinct fa.leg from FlightAssignment fa where fa.flightCrewMember.id = :memberId")
	Collection<Leg> findLegsByFlightCrewMemberId(int memberId);

	@Query("select fa from FlightAssignment fa where fa.leg.id = :legId")
	Collection<FlightAssignment> findFlightAssignmentByLegId(int legId);

	@Query("select l from Leg l where l.draftMode = false and l.scheduledDeparture > :now")
	Collection<Leg> findPublishedFutureLegs(Date now);

}
