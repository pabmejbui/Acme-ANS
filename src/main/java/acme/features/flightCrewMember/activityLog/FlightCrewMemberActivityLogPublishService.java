
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
public class FlightCrewMemberActivityLogPublishService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status = false;
		boolean completed = false;
		boolean method = true;
		ActivityLog log;
		int logId, memberId;

		// Bloquear si es GET
		if (super.getRequest().getMethod().equalsIgnoreCase("GET"))
			method = false;
		else {
			// Obtener ID del log y del usuario
			logId = super.getRequest().getData("id", int.class);
			log = this.repository.findActivityLogById(logId);
			memberId = super.getRequest().getPrincipal().getActiveRealm().getId();

			if (log != null) {
				// Verificar que el log sea del usuario y esté en modo borrador
				status = log.getFlightAssignment().getFlightCrewMember().getId() == memberId && log.isDraftMode();

				// Verificar que el vuelo haya terminado
				completed = MomentHelper.isBefore(log.getFlightAssignment().getLeg().getScheduledArrival(), MomentHelper.getCurrentMoment());
			}
		}

		super.getResponse().setAuthorised(status && completed && method);
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
		FlightAssignment assignment = log.getFlightAssignment();

		// 1. El vuelo debe estar publicado (draftMode == false)
		if (assignment.isDraftMode())
			super.state(false, "*", "acme.validation.activity-log.flight-assignment-not-published.message");

		// 2. El vuelo debe haber finalizado (scheduledArrival < ahora)
		if (!MomentHelper.isBefore(assignment.getLeg().getScheduledArrival(), MomentHelper.getCurrentMoment()))
			super.state(false, "*", "acme.validation.activity-log.flight-assignment-not-completed.message");
	}

	@Override
	public void perform(final ActivityLog log) {
		log.setDraftMode(false);
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

		super.getResponse().addData(dataset);
	}
}
