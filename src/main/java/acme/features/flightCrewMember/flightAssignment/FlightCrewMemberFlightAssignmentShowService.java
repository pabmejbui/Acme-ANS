
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
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


	@Override
	public void authorise() {
		boolean status;
		int assignmentId;
		FlightCrewMember member;
		FlightAssignment assignment;

		assignmentId = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(assignmentId);
		member = assignment == null ? null : assignment.getFlightCrewMember();
		status = member != null && super.getRequest().getPrincipal().hasRealm(member);

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

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;
		SelectChoices statuses;
		SelectChoices duties;
		Collection<Leg> legs;
		SelectChoices selectedLegs;
		String employeeCode;
		FlightCrewMember member;
		SelectChoices selectedMembers;

		FlightCrewMember activeMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		member = assignment.getFlightCrewMember();

		Collection<FlightCrewMember> crewMembers;
		crewMembers = this.repository.findCrewMembersByAirline(activeMember.getAirline());

		legs = this.repository.findPublishedFutureOwnedLegs(MomentHelper.getCurrentMoment(), member.getAirline());

		if (!crewMembers.contains(assignment.getFlightCrewMember()))
			crewMembers.add(assignment.getFlightCrewMember());

		Leg currentLeg = assignment.getLeg();
		if (!legs.contains(currentLeg))
			legs.add(currentLeg);

		employeeCode = assignment.getFlightCrewMember().getEmployeeCode();
		statuses = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
		duties = SelectChoices.from(DutyType.class, assignment.getDuty());
		selectedLegs = SelectChoices.from(legs, "flightNumber", assignment.getLeg());
		selectedMembers = SelectChoices.from(crewMembers, "employeeCode", assignment.getFlightCrewMember());

		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "draftMode");
		dataset.put("statuses", statuses);
		dataset.put("crewMembers", selectedMembers);
		dataset.put("employeeCode", employeeCode);
		dataset.put("duties", duties);
		dataset.put("leg", selectedLegs.getSelected().getKey());
		dataset.put("legs", selectedLegs);

		// 🔹 Añadido para mostrar el FlightCrewMember como campo de solo lectura
		if (assignment.getFlightCrewMember() != null)
			dataset.put("flightCrewMemberCode", assignment.getFlightCrewMember().getEmployeeCode());

		super.getResponse().addData(dataset);
	}

}
