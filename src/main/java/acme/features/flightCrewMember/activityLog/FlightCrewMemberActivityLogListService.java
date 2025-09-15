//
// package acme.features.flightCrewMember.activityLog;
//
// import java.util.Collection;
//
// import org.springframework.beans.factory.annotation.Autowired;
//
// import acme.client.components.models.Dataset;
// import acme.client.services.AbstractGuiService;
// import acme.client.services.GuiService;
// import acme.entities.activityLog.ActivityLog;
// import acme.realms.flightCrewMembers.FlightCrewMember;
//
// @GuiService
// public class FlightCrewMemberActivityLogListService extends AbstractGuiService<FlightCrewMember, ActivityLog> {
//
// @Autowired
// private FlightCrewMemberActivityLogRepository repository;
//
//
// @Override
// public void authorise() {
// super.getResponse().setAuthorised(true);
// }
//
// // @Override
// // public void load() {
// // FlightCrewMember member = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
// //
// // Collection<ActivityLog> logs = this.repository.findAllLogsByFlightCrewMemberId(member.getId());
// //
// // super.getBuffer().addData(logs);
// // }
// @Override
// public void load() {
// int masterId;
// Collection<ActivityLog> logs;
//
// masterId = super.getRequest().getData("masterId", int.class);
// logs = this.repository.findActivityLogsByMasterId(masterId);
//
// super.getBuffer().addData(logs);
// }
// @Override
// public void unbind(final ActivityLog log) {
// Dataset dataset;
//
// dataset = super.unbindObject(log, "incidentType", "severity", "registrationMoment");
// dataset.put("flightAssignment", log.getFlightAssignment().getId());
//
// super.addPayload(dataset, log, "description");
// super.getResponse().addData(dataset);
//
// }
// // @Override
// // public void unbind(final ActivityLog log) {
// // Dataset dataset;
// //
// // dataset = super.unbindObject(log, "incidentType", "severity", "registrationMoment");
// // dataset.put("flightAssignment", "assignment-" + log.getFlightAssignment().getId());
// //
// // super.addPayload(dataset, log, "description");
// // super.getResponse().addData(dataset);
// //
// // }
//
// }

package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogListService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		FlightAssignment assignment;

		masterId = super.getRequest().getData("masterId", int.class);
		assignment = this.repository.findFlightAssignmentById(masterId);
		status = assignment != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int masterId;
		int memberId;
		int ownerId;
		Collection<ActivityLog> logs;

		masterId = super.getRequest().getData("masterId", int.class);
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		ownerId = this.repository.findFlightAssignmentById(masterId).getFlightCrewMember().getId();
		if (ownerId == memberId)
			logs = this.repository.findActivityLogsByMasterId(masterId);
		else
			logs = this.repository.findPublishedActivityLogsByMasterId(masterId);

		super.getBuffer().addData(logs);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;
		SelectChoices selectedAssignments;
		Collection<FlightAssignment> assignments;
		String flightNumber;
		FlightCrewMember member;

		member = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		assignments = this.repository.findFlightAssignmentsByMemberIdOrPublished(member.getId());
		selectedAssignments = SelectChoices.from(assignments, "leg.flightNumber", log.getFlightAssignment());
		flightNumber = log.getFlightAssignment().getLeg().getFlightNumber();

		dataset = super.unbindObject(log, "incidentType", "severity", "draftMode");
		super.addPayload(dataset, log, "description", "registrationMoment");
		dataset.put("flightNumber", flightNumber);
		dataset.put("masterId", log.getFlightAssignment().getId());
		dataset.put("assignments", selectedAssignments);
		dataset.put("assignment", selectedAssignments.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<ActivityLog> log) {
		int masterId;
		FlightAssignment assignment;
		boolean correctFlightCrewMember;
		int memberId;
		int userId;

		masterId = super.getRequest().getData("masterId", int.class);
		assignment = this.repository.findFlightAssignmentById(masterId);
		userId = super.getRequest().getPrincipal().getActiveRealm().getId();
		memberId = assignment.getFlightCrewMember().getId();

		correctFlightCrewMember = memberId == userId;

		super.getResponse().addGlobal("masterId", masterId);
		super.getResponse().addGlobal("showingCreate", correctFlightCrewMember);

	}

}
