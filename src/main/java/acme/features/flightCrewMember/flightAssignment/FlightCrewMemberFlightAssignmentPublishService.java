
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
public class FlightCrewMemberFlightAssignmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	//	@Override
	//	public void authorise() {
	//		super.getResponse().setAuthorised(true);
	//	}
	@Override
	public void authorise() {
		FlightAssignment assignment;
		boolean validRequestMethod = true;
		boolean validStatus = true;
		boolean validDuty = true;
		boolean validLeg = true;
		boolean correctMember = false;
		int assignmentId;

		if (super.getRequest().getMethod().equals("GET"))
			validRequestMethod = false;
		else {
			// Validar que el duty existe en el enum
			String dutyString = super.getRequest().getData("duty", String.class);
			validDuty = Arrays.stream(DutyType.values()).anyMatch(d -> d.toString().equals(dutyString));
			validDuty = validDuty || dutyString.equals("0");  // Para cuando aún no se ha seleccionado nada

			// Validar que el status existe en el enum
			String statusString = super.getRequest().getData("status", String.class);
			validStatus = Arrays.stream(AssignmentStatus.values()).anyMatch(s -> s.toString().equals(statusString));
			validStatus = validStatus || statusString.equals("0");

			// Validar que el Leg es válido
			int legId = super.getRequest().getData("leg", int.class);
			Collection<Leg> validLegs = this.repository.findAllLegs();
			validLeg = legId != 0 && validLegs.stream().anyMatch(l -> l.getId() == legId);

			// Validar que el assignment pertenece al usuario y está en draft
			assignmentId = super.getRequest().getData("id", int.class);
			assignment = this.repository.findFlightAssignmentById(assignmentId);

			if (assignment != null) {
				int principalId = super.getRequest().getPrincipal().getActiveRealm().getId();
				correctMember = assignment.getFlightCrewMember().getId() == principalId && assignment.isDraftMode();
			}
		}

		boolean authorised = validRequestMethod && validDuty && validStatus && validLeg && correctMember;
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.repository.findFlightAssignmentById(id);
		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		Integer legId;
		Leg leg;
		Integer memberId;
		FlightCrewMember member;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);
		memberId = super.getRequest().getData("member", int.class);
		member = this.repository.findFlightCrewMemberById(memberId);

		super.bindObject(assignment, "duty", "status", "remarks");
		assignment.setLeg(leg);
		assignment.setFlightCrewMember(member);
		assignment.setLastUpdate(MomentHelper.getCurrentMoment());
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		if (super.getRequest().getData("leg", int.class) != 0) {
			boolean notYetOccurred = MomentHelper.isAfter(assignment.getLeg().getScheduledArrival(), MomentHelper.getCurrentMoment());
			super.state(notYetOccurred, "leg", "flight-crew-member.flight-assignment.leg-has-not-finished-yet");
		}
		Leg leg = assignment.getLeg();

		if (leg == null)
			super.state(false, "leg", "flightAssignment.error.leg-required");
		else {
			boolean isDraft = leg.isDraftMode();  // true si está en borrador (no publicado)
			super.state(!isDraft, "leg", "flightAssignment.error.leg-not-published");
		}

	}

	@Override
	public void perform(final FlightAssignment assignment) {
		assignment.setDraftMode(false);
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
}
