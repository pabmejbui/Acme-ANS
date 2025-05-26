
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;
import acme.entities.airlines.Airline;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flights.Leg;
import acme.realms.flightCrewMembers.FlightCrewMember;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select fa from FlightAssignment fa where fa.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :memberId and fa.leg.scheduledArrival < :now")
	Collection<FlightAssignment> findMyCompletedAssignments(Date now, Integer memberId);

	@Query("select fa from FlightAssignment fa where fa.draftMode = false and fa.leg.scheduledArrival < :now")
	Collection<FlightAssignment> findCompletedPublishedAssignments(Date now);

	@Query("select fa from FlightAssignment fa where fa.draftMode = false and fa.leg.scheduledArrival > :now")
	Collection<FlightAssignment> findPlannedPublishedAssignments(Date now);

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :memberId and fa.leg.scheduledArrival > :now")
	Collection<FlightAssignment> findMyPlannedAssignments(Date now, Integer memberId);

	@Query("select l from Leg l where l.draftMode = false and l.scheduledArrival > :now and l.aircraft.airline = :airline")
	Collection<Leg> findPublishedFutureOwnedLegs(Date now, Airline airline);

	@Query("select l from Leg l where l.draftMode = false")
	Collection<Leg> findPublishedLegs();

	@Query("select l from Leg l where l.id = :legId")
	Leg findLegById(int legId);

	@Query("select l from ActivityLog l where l.flightAssignment.id = :id")
	Collection<ActivityLog> findActivityLogsByAssignmentId(int id);

	@Query("select distinct fa.leg from FlightAssignment fa where fa.flightCrewMember.id = :memberId")
	Collection<Leg> findLegsByFlightCrewMemberId(int memberId);

	@Query("select fa from FlightAssignment fa where fa.leg.id = :legId")
	Collection<FlightAssignment> findFlightAssignmentByLegId(int legId);

	@Query("select l from Leg l")
	Collection<Leg> findAllLegs();

	@Query("select m from FlightCrewMember m")
	Collection<FlightCrewMember> findAllFlightCrewMembers();

	@Query("select m from FlightCrewMember m where m.id = :memberId")
	FlightCrewMember findFlightCrewMemberById(int memberId);

}
