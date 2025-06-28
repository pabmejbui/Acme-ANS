
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
public class FlightCrewMemberFlightAssignmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
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

	//	@Override
	//	public void validate(final FlightAssignment assignment) {
	//		this.validateLegHasNotOccurred(assignment.getLeg());
	//		this.validateStatusAvailability(assignment.getFlightCrewMember());
	//		this.validateLegCompatibility(assignment);
	//		this.validateDutyAssignment(assignment);
	//	}
	//
	//	private void validateLegHasNotOccurred(final Leg leg) {
	//		Date scheduledArrival = leg.getScheduledArrival();
	//		Date now = MomentHelper.getCurrentMoment();
	//		boolean hasOccurred = now.after(scheduledArrival);
	//		super.state(!hasOccurred, "*", "acme.validation.flight-assignment.leg-has-occurred.message. Cant validate a past flight");
	//	}
	//
	//	private void validateStatusAvailability(final FlightCrewMember member) {
	//		AvailabilityStatus status = member.getAvailabilityStatus();
	//		boolean available = status.equals(AvailabilityStatus.AVAILABLE);
	//		super.state(available, "*", "acme.validation.flight-assignment.member-not-available.message");
	//	}
	//
	//	private void validateLegCompatibility(final FlightAssignment assignment) {
	//		Collection<Leg> existingLegs = this.repository.findLegsByFlightCrewMemberId(assignment.getFlightCrewMember().getId());
	//
	//		boolean overlaps = existingLegs.stream().anyMatch(existingLeg -> this.legsOverlap(assignment.getLeg(), existingLeg));
	//
	//		super.state(!overlaps, "*", "acme.validation.flight-assignment.member-with-overlapping-legs.message");
	//	}
	//
	//	private boolean legsOverlap(final Leg newLeg, final Leg existingLeg) {
	//		boolean isDepartureOverlapping = MomentHelper.isInRange(newLeg.getScheduledDeparture(), existingLeg.getScheduledDeparture(), existingLeg.getScheduledArrival());
	//
	//		boolean isArrivalOverlapping = MomentHelper.isInRange(newLeg.getScheduledArrival(), existingLeg.getScheduledDeparture(), existingLeg.getScheduledArrival());
	//
	//		return isDepartureOverlapping || isArrivalOverlapping;
	//	}
	//
	//	private void validateDutyAssignment(final FlightAssignment flightAssignment) {
	//		Collection<FlightAssignment> assignedDuties = this.repository.findFlightAssignmentByLegId(flightAssignment.getLeg().getId());
	//
	//		boolean legHasPilot = assignedDuties.stream().anyMatch(a -> a.getDuty().equals(DutyType.PILOT));
	//
	//		boolean legHasCoPilot = assignedDuties.stream().anyMatch(a -> a.getDuty().equals(DutyType.CO_PILOT));
	//
	//		super.state(!(flightAssignment.getDuty().equals(DutyType.PILOT) && legHasPilot), "*", "acme.validation.flight-assignment.leg-has-pilot.message");
	//
	//		super.state(!(flightAssignment.getDuty().equals(DutyType.CO_PILOT) && legHasCoPilot), "*", "acme.validation.flight-assignment.leg-has-copilot.message");
	//	}
	@Override
	public void validate(final FlightAssignment assignment) {
		if (super.getRequest().getData("leg", int.class) != 0) {
			boolean notYetOccurred = MomentHelper.isAfter(assignment.getLeg().getScheduledArrival(), MomentHelper.getCurrentMoment());
			super.state(notYetOccurred, "leg", "flight-crew-member.flight-assignment.leg-has-not-finished-yet");
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
