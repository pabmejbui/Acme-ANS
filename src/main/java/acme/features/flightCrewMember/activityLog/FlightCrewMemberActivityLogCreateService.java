
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

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {

		boolean isAuthorised = false;

		try {
			// Only is allowed to create an activity log if the creator is the flight crew member associated to the flight assignment.
			// An activity log cannot be created if the assignment is planned, only complete are allowed.
			Integer assignmentId = super.getRequest().getData("assignmentId", Integer.class);
			if (assignmentId != null) {
				FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(assignmentId);
				isAuthorised = flightAssignment != null && flightAssignment.getLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment()) && super.getRequest().getPrincipal().hasRealm(flightAssignment.getFlightCrewMember());
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		ActivityLog log = new ActivityLog();

		log.setRegistrationMoment(MomentHelper.getCurrentMoment());

		int assignmentId = super.getRequest().getData("assignmentId", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(assignmentId);
		log.setFlightAssignment(flightAssignment);

		log.setDraftMode(true);
		super.getBuffer().addData(log);
	}

	@Override
	public void bind(final ActivityLog log) {
		assert log != null;
		super.bindObject(log, "incidentType", "description", "severity");
	}

	@Override
	public void validate(final ActivityLog log) {
		assert log != null;
	}

	@Override
	public void perform(final ActivityLog log) {
		assert log != null;

		this.repository.save(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		assert log != null;

		Dataset dataset = super.unbindObject(log, "registrationMoment", "incidentType", "description", "severity", "draftMode");

		// Show create if the assignment is completed
		if (log.getFlightAssignment().getLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment()))
			super.getResponse().addGlobal("showAction", true);

		dataset.put("assignmentId", super.getRequest().getData("assignmentId", int.class));
		super.getResponse().addData(dataset);
	}
}
