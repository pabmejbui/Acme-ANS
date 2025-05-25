
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
public class FlightCrewMemberFlightAssignmentUpdateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		boolean validLeg;
		int masterId;
		FlightAssignment assignment;
		FlightCrewMember member;

		masterId = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(masterId);
		member = assignment == null ? null : assignment.getFlightCrewMember();
		status = assignment != null && assignment.isDraftMode() && super.getRequest().getPrincipal().hasRealm(member);

		if (super.getRequest().getMethod().equals("POST")) {
			int legId = super.getRequest().getData("leg", int.class);
			Leg leg = this.repository.findLegById(legId);

			validLeg = legId == 0 || leg != null;
			if (validLeg && leg != null) {
				boolean isSameAsAssigned = assignment.getLeg() != null && leg.getId() == assignment.getLeg().getId();
				boolean isFuture = MomentHelper.isBefore(MomentHelper.getCurrentMoment(), leg.getScheduledArrival());
				boolean isMyAirline = leg.getAircraft().getAirline().getId() == member.getAirline().getId();
				validLeg = !leg.isDraftMode() && isMyAirline && (isFuture || isSameAsAssigned);
			}
			status = status && validLeg;
		}

		super.getResponse().setAuthorised(status);
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
	public void bind(final FlightAssignment assignment) {
		int legId;
		Leg leg;
		FlightCrewMember member;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);
		member = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		super.bindObject(assignment, "duty", "status", "remarks");
		assignment.setLastUpdate(MomentHelper.getCurrentMoment());
		assignment.setLeg(leg);
		assignment.setFlightCrewMember(member);
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
		String employeeCode;
		FlightCrewMember member;

		member = assignment.getFlightCrewMember();

		legs = this.repository.findPublishedFutureOwnedLegs(MomentHelper.getCurrentMoment(), member.getAirline());
		Leg currentLeg = assignment.getLeg();
		if (currentLeg != null && !legs.contains(currentLeg))
			legs.add(currentLeg);
		statuses = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
		duties = SelectChoices.from(DutyType.class, assignment.getDuty());
		selectedLegs = SelectChoices.from(legs, "flightNumber", assignment.getLeg());
		employeeCode = assignment.getFlightCrewMember().getEmployeeCode();

		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "draftMode");
		dataset.put("employeeCode", employeeCode);
		dataset.put("statuses", statuses);
		dataset.put("duties", duties);
		dataset.put("leg", selectedLegs.getSelected().getKey());
		dataset.put("legs", selectedLegs);

		super.getResponse().addData(dataset);
	}
}
