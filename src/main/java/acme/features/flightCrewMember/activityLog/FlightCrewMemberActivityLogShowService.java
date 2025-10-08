
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

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
public class FlightCrewMemberActivityLogShowService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean authorised = false;

		if (super.getRequest().hasData("id")) {
			int logId = super.getRequest().getData("id", int.class);
			ActivityLog log = this.repository.findActivityLogById(logId);

			if (log != null) {
				boolean isOwner = super.getRequest().getPrincipal().hasRealm(log.getFlightAssignment().getFlightCrewMember());
				boolean flightFinished = MomentHelper.isBefore(log.getFlightAssignment().getLeg().getScheduledArrival(), MomentHelper.getCurrentMoment());
				authorised = isOwner && flightFinished;
			}
		}

		super.getResponse().setAuthorised(authorised);

	}

	@Override
	public void load() {
		int logId = super.getRequest().getData("id", int.class);
		ActivityLog log = this.repository.findActivityLogById(logId);
		super.getBuffer().addData(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;
		Collection<FlightAssignment> assignments;
		SelectChoices assignmentChoices;

		int userId = super.getRequest().getPrincipal().getActiveRealm().getId();
		assignments = this.repository.findPublishedAssignmentsByFlightCrewMemberId(userId);

		// Nos aseguramos de que el assignment del log esté incluido aunque sea draft
		if (!assignments.contains(log.getFlightAssignment()))
			assignments.add(log.getFlightAssignment());

		assignmentChoices = SelectChoices.from(assignments, "id", log.getFlightAssignment());

		dataset = super.unbindObject(log, "registrationMoment", "incidentType", "description", "severity", "draftMode");
		dataset.put("assignments", assignmentChoices);
		dataset.put("assignment", assignmentChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
