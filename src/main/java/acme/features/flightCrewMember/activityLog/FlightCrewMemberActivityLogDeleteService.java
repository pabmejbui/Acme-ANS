
package acme.features.flightCrewMember.activityLog;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogDeleteService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int logId;
		FlightCrewMember member;
		ActivityLog log;

		logId = super.getRequest().getData("id", int.class);
		log = this.repository.findActivityLogById(logId);
		member = log == null ? null : log.getFlightAssignment().getFlightCrewMember();
		status = member != null && super.getRequest().getPrincipal().hasRealm(member) && log.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog log;
		int logId;

		logId = super.getRequest().getData("id", int.class);
		log = this.repository.findActivityLogById(logId);

		super.getBuffer().addData(log);
	}

	@Override
	public void bind(final ActivityLog log) {
		Date now;
		int assignmentId;
		FlightAssignment assignment;

		assignmentId = super.getRequest().getData("assignment", int.class);
		assignment = this.repository.findFlightAssignmentById(assignmentId);
		now = MomentHelper.getCurrentMoment();

		super.bindObject(log, "incidentType", "description", "severity");
		log.setRegistrationMoment(now);
		log.setFlightAssignment(assignment);
	}

	@Override
	public void validate(final ActivityLog log) {
		;
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		this.repository.delete(activityLog);
	}

	//	@Override
	//	public void unbind(final ActivityLog log) {
	//		Dataset dataset;
	//		SelectChoices selectedAssignments;
	//		Collection<FlightAssignment> assignments;
	//
	//		assignments = this.repository.findAllAssignments();
	//		selectedAssignments = SelectChoices.from(assignments, "id", log.getFlightAssignment());
	//
	//		dataset = super.unbindObject(log, "registrationMoment", "incidentType", "description", "severity", "draftMode");
	//		dataset.put("assignments", selectedAssignments);
	//		dataset.put("assignment", selectedAssignments.getSelected().getKey());
	//
	//		super.getResponse().addData(dataset);
	//	}

}
