
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogListService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean isAuthorised = false;

		try {
			// Only is allowed to view an activity log list if the creator is the flight crew member associated to the flight assignment.
			Integer assignmentId = super.getRequest().getData("assignmentId", Integer.class);
			if (assignmentId != null) {
				FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(assignmentId);
				isAuthorised = flightAssignment != null && super.getRequest().getPrincipal().hasRealm(flightAssignment.getFlightCrewMember());
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		int assignmentId = super.getRequest().getData("assignmentId", int.class);
		Collection<ActivityLog> activityLogs = this.repository.findActivityLogsByFlightAssignmentId(assignmentId);
		super.getBuffer().addData(activityLogs);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		assert activityLog != null;

		Dataset dataset = super.unbindObject(activityLog, "registrationlastUpdate", "incidentType", "description", "severity", "draftMode");

		int assignmentId = super.getRequest().getData("assignmentId", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(assignmentId);

		// Show create if the assignment is completed
		if (flightAssignment.getLeg().getScheduledArrival().before(MomentHelper.getCurrentlastUpdate()))
			super.getResponse().addGlobal("showAction", true);

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<ActivityLog> activityLogs) {
		assert activityLogs != null;

		int assignmentId = super.getRequest().getData("assignmentId", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(assignmentId);

		// Show create if the assignment is completed
		if (flightAssignment.getLeg().getScheduledArrival().before(MomentHelper.getCurrentlastUpdate()))
			super.getResponse().addGlobal("showAction", true);

		super.getResponse().addGlobal("assignmentId", super.getRequest().getData("assignmentId", int.class));
	}

}
