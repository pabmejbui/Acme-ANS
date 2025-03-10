
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightCrewMemberDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	List<String>				lastFiveDestinations;

	Integer						legsWithSeverity0to3;
	Integer						legsWithSeverity4to7;
	Integer						legsWithSeverity8to10;

	List<String>				crewMembersLastLeg;

	Map<String, Integer>		flightAssignmentsByStatus;

	Double						averageFlightAssignmentsLastMonth;
	Integer						minFlightAssignmentsLastMonth;
	Integer						maxFlightAssignmentsLastMonth;
	Double						standardDeviationFlightAssignmentsLastMonth;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}
