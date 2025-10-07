
package acme.features.flightCrewMember.flightAssignment;

import java.util.Arrays;
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
public class FlightCrewMemberFlightAssignmentCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean isPost = super.getRequest().getMethod().equals("POST");

		boolean correctDuty = true;
		boolean correctStatus = true;
		boolean transientId = true;
		boolean validLeg = true;

		if (isPost) {
			String duty = super.getRequest().getData("duty", String.class);
			correctDuty = Arrays.stream(DutyType.values()).anyMatch(d -> d.toString().equals(duty)) || duty.equals("0");

			String status = super.getRequest().getData("status", String.class);
			correctStatus = Arrays.stream(AssignmentStatus.values()).anyMatch(s -> s.toString().equals(status)) || status.equals("0");

			transientId = super.getRequest().getData("id", int.class) == 0;

			if (super.getRequest().getData().containsKey("leg")) {
				int legId = super.getRequest().getData("leg", int.class);

				if (legId != 0) {
					Leg leg = this.repository.findLegById(legId);
					FlightCrewMember activeMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

					validLeg = leg != null && leg.getAircraft().getAirline().equals(activeMember.getAirline());
				}
			}

		}

		super.getResponse().setAuthorised(correctDuty && correctStatus && transientId && validLeg);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		flightAssignment = new FlightAssignment();
		flightAssignment.setFlightCrewMember(flightCrewMember);
		flightAssignment.setLastUpdate(MomentHelper.getCurrentMoment());
		flightAssignment.setDraftMode(true);
		super.getBuffer().addData(flightAssignment);

	}

	@Override
	public void bind(final FlightAssignment assignment) {
		int legId = super.getRequest().getData("leg", int.class);
		Leg leg = this.repository.findLegById(legId);

		super.bindObject(assignment, "duty", "status", "remarks");
		assignment.setLeg(leg);
		assignment.setLastUpdate(MomentHelper.getCurrentMoment());
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		;
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;
		SelectChoices statuses;
		SelectChoices duties;
		Collection<Leg> legs;
		SelectChoices selectedLegs;
		Collection<FlightCrewMember> members;
		SelectChoices selectedMembers;

		FlightCrewMember activeMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		legs = this.repository.findPublishedFutureOwnedLegs(MomentHelper.getCurrentMoment(), activeMember.getAirline());
		members = this.repository.findCrewMembersByAirline(activeMember.getAirline());

		statuses = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
		duties = SelectChoices.from(DutyType.class, assignment.getDuty());
		selectedLegs = SelectChoices.from(legs, "flightNumber", assignment.getLeg());
		selectedMembers = SelectChoices.from(members, "employeeCode", assignment.getFlightCrewMember());

		dataset = super.unbindObject(assignment, "duty", "status", "remarks", "draftMode", "lastUpdate");

		dataset.put("statuses", statuses);
		dataset.put("duties", duties);

		if (selectedLegs.getSelected() != null)
			dataset.put("leg", selectedLegs.getSelected().getKey());
		dataset.put("legs", selectedLegs);

		if (selectedMembers.getSelected() != null)
			dataset.put("flightCrewMember", selectedMembers.getSelected().getKey());
		dataset.put("crewMembers", selectedMembers);

		if (assignment.getFlightCrewMember() != null)
			dataset.put("flightCrewMemberCode", assignment.getFlightCrewMember().getEmployeeCode());

		if (assignment.getLastUpdate() != null)
			dataset.put("lastUpdate", assignment.getLastUpdate());

		super.getResponse().addData(dataset);
	}

}
