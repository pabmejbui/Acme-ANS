
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airlines.Airline;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flights.Leg;
import acme.realms.flightCrewMembers.FlightCrewMember;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select fa from FlightAssignment fa where fa.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select fa from FlightAssignment fa where fa.leg.scheduledArrival < :now and fa.flightCrewMember.id = :id")
	Collection<FlightAssignment> findCompletedFlightAssignmentsByMemberId(Date now, int id);

	@Query("select fa from FlightAssignment fa where fa.leg.scheduledDeparture > :now and fa.flightCrewMember.id = :id")
	Collection<FlightAssignment> findPlannedFlightAssignmentsByMemberId(Date now, int id);

	@Query("select l from Leg l where l.id = :legId")
	Leg findLegById(int legId);

	@Query("select l from Leg l where l.draftMode = false and l.scheduledArrival > :now and l.aircraft.airline = :airline")
	Collection<Leg> findPublishedFutureOwnedLegs(Date now, Airline airline);

	@Query("select fcm from FlightCrewMember fcm where fcm.airline = :airline")
	Collection<FlightCrewMember> findCrewMembersByAirline(Airline airline);

}
