
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogCreateService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean isAuthorised = false;

		try {
			// Only is allowed to create an activity log if the creator is the flight crew member associated to the flight assignment.
			// An activity log cannot be created if the assignment is planned, only complete are allowed.
			Integer assignmentId = super.getRequest().getData("assignmentId", Integer.class);
			if (assignmentId != null) {
				FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(assignmentId);
				isAuthorised = flightAssignment != null && flightAssignment.getLeg().getScheduledArrival().before(MomentHelper.getCurrentlastUpdate()) && super.getRequest().getPrincipal().hasRealm(flightAssignment.getFlightCrewMember());
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		ActivityLog activityLog = new ActivityLog();

		activityLog.setRegistrationMoment(MomentHelper.getCurrentlastUpdate());

		int assignmentId = super.getRequest().getData("assignmentId", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(assignmentId);
		activityLog.setFlightAssignment(flightAssignment);

		activityLog.setDraftMode(true);
		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		assert activityLog != null;

		super.bindObject(activityLog, "incidentType", "description", "severity");
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		assert activityLog != null;
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		assert activityLog != null;

		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		assert activityLog != null;

		Dataset dataset = super.unbindObject(activityLog, "registrationlastUpdate", "incidentType", "description", "severity", "draftMode");

		// Show create if the assignment is completed
		if (activityLog.getFlightAssignment().getLeg().getScheduledArrival().before(MomentHelper.getCurrentlastUpdate()))
			super.getResponse().addGlobal("showAction", true);

		dataset.put("assignmentId", super.getRequest().getData("assignmentId", int.class));
		super.getResponse().addData(dataset);
	}

}
