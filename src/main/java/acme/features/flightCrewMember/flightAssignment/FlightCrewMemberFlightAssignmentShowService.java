
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


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		FlightAssignment assignment;
		int id;

		id = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(id);

		super.getBuffer().addData(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;
		SelectChoices statuses;
		SelectChoices duties;
		SelectChoices selectedLegs;
		Collection<Leg> legs;
		SelectChoices selectedMembers;
		Collection<FlightCrewMember> members;

		statuses = SelectChoices.from(AssignmentStatus.class, assignment.getCurrentStatus());
		duties = SelectChoices.from(DutyType.class, assignment.getDuty());
		legs = this.repository.findAllLegs();
		selectedLegs = SelectChoices.from(legs, "flightNumber", assignment.getLeg());
		members = this.repository.findAllFlightCrewMembers();
		selectedMembers = SelectChoices.from(members, "employeeCode", assignment.getFlightCrewMember());

		dataset = super.unbindObject(assignment, "lastUpdateMoment", "remarks");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("statuses", statuses);
		dataset.put("duties", duties);
		dataset.put("legs", selectedLegs);
		dataset.put("leg", selectedLegs.getSelected().getKey());
		dataset.put("members", members);
		dataset.put("member", selectedMembers.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
