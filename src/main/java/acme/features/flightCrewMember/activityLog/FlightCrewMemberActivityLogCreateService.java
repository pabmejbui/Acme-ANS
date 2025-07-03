
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
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
		boolean status = false;
		int userId, assignmentId;
		FlightAssignment assignment;

		userId = super.getRequest().getPrincipal().getActiveRealm().getId();

		// Solo comprobamos el assignment en POST
		if (super.getRequest().getMethod().equalsIgnoreCase("POST")) {
			if (super.getRequest().getData().containsKey("assignment")) {
				assignmentId = super.getRequest().getData("assignment", int.class);
				assignment = this.repository.findFlightAssignmentById(assignmentId);
				status = assignment != null && assignment.getFlightCrewMember().getId() == userId;
			}
		} else
			// En GET basta con que esté autenticado
			status = true;

		super.getResponse().setAuthorised(status);
	}
	@Override
	public void load() {
		ActivityLog log;

		log = new ActivityLog();
		log.setDraftMode(true);

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
		log.setDraftMode(true);
	}

	@Override
	public void validate(final ActivityLog log) {
		FlightAssignment assignment = log.getFlightAssignment();
		Date now = MomentHelper.getCurrentMoment();
		Date scheduledArrival = assignment.getLeg().getScheduledArrival();

		// Validar que el assignment esté publicado
		super.state(!assignment.isDraftMode(), "assignment", "acme.validation.activity-log.assignment-not-published");

		// Validar que la leg asociada al assignment esté publicada
		super.state(!assignment.getLeg().isDraftMode(), "assignment", "acme.validation.activity-log.leg-not-published");

		// Validar que la leg haya terminado (es decir, que ya haya llegado)
		boolean legHasEnded = MomentHelper.isBefore(scheduledArrival, now);
		super.state(legHasEnded, "assignment", "acme.validation.activity-log.leg-not-ended");

	}
	@Override
	public void perform(final ActivityLog log) {
		this.repository.save(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;
		SelectChoices selectedAssignments;
		Collection<FlightAssignment> assignments;

		int userId = super.getRequest().getPrincipal().getActiveRealm().getId();
		assignments = this.repository.findAssignmentsByFlightCrewMemberId(userId);
		selectedAssignments = SelectChoices.from(assignments, "id", log.getFlightAssignment());

		dataset = super.unbindObject(log, "incidentType", "description", "severity", "draftMode");
		dataset.put("assignments", selectedAssignments);
		dataset.put("assignment", selectedAssignments.getSelected().getKey());
		dataset.put("readonly", false);
		super.getResponse().addData(dataset);
	}

}
