//
// package acme.features.flightCrewMember.flightAssignment;
//
// import java.util.Collection;
//
// import org.springframework.beans.factory.annotation.Autowired;
//
// import acme.client.components.models.Dataset;
// import acme.client.components.views.SelectChoices;
// import acme.client.helpers.MomentHelper;
// import acme.client.services.AbstractGuiService;
// import acme.client.services.GuiService;
// import acme.entities.flightAssignment.AssignmentStatus;
// import acme.entities.flightAssignment.DutyType;
// import acme.entities.flightAssignment.FlightAssignment;
// import acme.entities.flights.Leg;
// import acme.realms.flightCrewMembers.FlightCrewMember;
//
// @GuiService
// public class FlightCrewMemberFlightAssignmentShowService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {
//
// @Autowired
// private FlightCrewMemberFlightAssignmentRepository repository;
//
//
// @Override
// public void authorise() {
// boolean status;
// int masterId;
// FlightAssignment assignment;
// FlightCrewMember member;
//
// masterId = super.getRequest().getData("id", int.class);
// assignment = this.repository.findFlightAssignmentById(masterId);
// member = assignment == null ? null : assignment.getFlightCrewMember();
// status = assignment != null;
//
// status = status && (!assignment.isDraftMode() || super.getRequest().getPrincipal().hasRealm(member));
//
// super.getResponse().setAuthorised(status);
// }
//
// @Override
// public void load() {
// FlightAssignment assignment;
// int assignmentId;
//
// assignmentId = super.getRequest().getData("id", int.class);
// assignment = this.repository.findFlightAssignmentById(assignmentId);
//
// super.getBuffer().addData(assignment);
// }
//
// @Override
// public void unbind(final FlightAssignment assignment) {
// Dataset dataset;
// SelectChoices statuses;
// SelectChoices duties;
// Collection<Leg> legs;
// SelectChoices selectedLegs;
// String employeeCode;
// FlightCrewMember member;
//
// member = assignment.getFlightCrewMember();
//
// if (assignment.isDraftMode()) {
// legs = this.repository.findPublishedFutureOwnedLegs(MomentHelper.getCurrentMoment(), member.getAirline());
//
// Leg currentLeg = assignment.getLeg();
// if (currentLeg != null && !legs.contains(currentLeg))
// legs.add(currentLeg);
// } else
// legs = this.repository.findPublishedLegs();
//
// employeeCode = assignment.getFlightCrewMember().getEmployeeCode();
// statuses = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
// duties = SelectChoices.from(DutyType.class, assignment.getDuty());
// selectedLegs = SelectChoices.from(legs, "flightNumber", assignment.getLeg());
//
// dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "draftMode");
// dataset.put("statuses", statuses);
// dataset.put("employeeCode", employeeCode);
// dataset.put("duties", duties);
// dataset.put("leg", selectedLegs.getSelected().getKey());
// dataset.put("legs", selectedLegs);
//
// super.getResponse().addData(dataset);
// }
//
// }

package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.AssignmentStatus;
import acme.entities.flightAssignment.DutyType;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flights.Leg;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentShowService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;

	// Es el authorise de d03
	// @Override
	// public void authorise() {
	// boolean status;
	// int assignmentId;
	// FlightCrewMember member;
	// FlightAssignment assignment;
	//
	// assignmentId = super.getRequest().getData("id", int.class);
	// assignment = this.repository.findFlightAssignmentById(assignmentId);
	// member = assignment == null ? null : assignment.getFlightCrewMember();
	// status = member != null && super.getRequest().getPrincipal().hasRealm(member);
	//
	// super.getResponse().setAuthorised(status);
	// }


	// es el authorise de d04
	@Override
	public void authorise() {
		boolean status;
		int masterId;
		FlightAssignment assignment;
		FlightCrewMember member;

		masterId = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(masterId);
		member = assignment == null ? null : assignment.getFlightCrewMember();
		status = assignment != null;

		status = status && (!assignment.isDraftMode() || super.getRequest().getPrincipal().hasRealm(member));

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment assignment;
		int assignmentId;

		assignmentId = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(assignmentId);

		super.getBuffer().addData(assignment);
	}

	//unbind d03
	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;
		SelectChoices statuses;
		SelectChoices duties;
		Collection<Leg> legs;
		SelectChoices selectedLegs;
		Collection<FlightCrewMember> members;
		SelectChoices selectedMembers;

		legs = this.repository.findAllLegs();
		members = this.repository.findAllFlightCrewMembers();

		statuses = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
		duties = SelectChoices.from(DutyType.class, assignment.getDuty());
		selectedLegs = SelectChoices.from(legs, "flightNumber", assignment.getLeg());
		selectedMembers = SelectChoices.from(members, "employeeCode", assignment.getFlightCrewMember());

		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "draftMode");
		dataset.put("statuses", statuses);
		dataset.put("duties", duties);
		dataset.put("leg", selectedLegs.getSelected().getKey());
		dataset.put("legs", selectedLegs);
		dataset.put("member", selectedMembers.getSelected().getKey());
		dataset.put("members", selectedMembers);

		super.getResponse().addData(dataset);
	}

	//unbind d04
	//	@Override
	//	public void unbind(final FlightAssignment assignment) {
	//		Dataset dataset;
	//		SelectChoices statuses;
	//		SelectChoices duties;
	//		Collection<Leg> legs;
	//		SelectChoices selectedLegs;
	//		String employeeCode;
	//		FlightCrewMember member;
	//
	//		member = assignment.getFlightCrewMember();
	//
	//		if (assignment.isDraftMode()) {
	//			legs = this.repository.findPublishedFutureOwnedLegs(MomentHelper.getCurrentMoment(), member.getAirline());
	//
	//			Leg currentLeg = assignment.getLeg();
	//			if (currentLeg != null && !legs.contains(currentLeg))
	//				legs.add(currentLeg);
	//		} else
	//			legs = this.repository.findPublishedLegs();
	//
	//		employeeCode = assignment.getFlightCrewMember().getEmployeeCode();
	//		statuses = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
	//		duties = SelectChoices.from(DutyType.class, assignment.getDuty());
	//		selectedLegs = SelectChoices.from(legs, "flightNumber", assignment.getLeg());
	//
	//		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "draftMode");
	//		dataset.put("statuses", statuses);
	//		dataset.put("employeeCode", employeeCode);
	//		dataset.put("duties", duties);
	//		dataset.put("leg", selectedLegs.getSelected().getKey());
	//		dataset.put("legs", selectedLegs);
	//
	//		super.getResponse().addData(dataset);
	//	}

}
