
package acme.features.flightCrewMember.flightAssignment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.AssignmentStatus;
import acme.entities.flightAssignment.DutyType;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightCrewMembers.AvailabilityStatus;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean isAuthorised = false;

		try {
			// Only is allowed to publish a flight assignment if the creator is associated.
			// A flight assignment cannot be published if the assignment is in published mode and not in draft mode.
			Integer flightAssignmentId = super.getRequest().getData("id", Integer.class);
			if (flightAssignmentId != null) {
				FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);
				isAuthorised = flightAssignment != null && flightAssignment.isDraftMode() && super.getRequest().getPrincipal().hasRealm(flightAssignment.getFlightCrewMember());
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		int flightAssignmentId = super.getRequest().getData("id", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		assert flightAssignment != null;

		super.bindObject(flightAssignment, "duty", "status", "remarks", "leg");
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		assert flightAssignment != null;

		if (flightAssignment.getFlightCrewMember() != null) {
			// Only flight crew members with an "AVAILABLE" status can be assigned 
			boolean isAvailable = flightAssignment.getFlightCrewMember().getAvailabilityStatus().equals(AvailabilityStatus.AVAILABLE);
			super.state(isAvailable, "flightCrewMember", "acme.validation.flightAssignment.flightCrewMember.available");

			// Cannot be assigned to multiple legs simultaneously
			boolean isAlreadyAssigned = this.repository.hasFlightCrewMemberLegAssociated(flightAssignment.getFlightCrewMember().getId(), MomentHelper.getCurrentMoment());
			super.state(!isAlreadyAssigned, "flightCrewMember", "acme.validation.flightAssignment.flightCrewMember.multipleLegs");
		}

		// To publish a flight assignment these cannot be linked to legs that are not published, do not belongs to the flight crew member airline or have already occurred
		if (flightAssignment.getLeg() != null) {
			boolean isDraftModeLeg = flightAssignment.getLeg().isDraftMode();
			super.state(!isDraftModeLeg, "leg", "acme.validation.flightAssignment.leg.draftmode");

			boolean isWrongAirlineLeg = flightAssignment.getLeg().getAircraft().getAirline().equals(flightAssignment.getFlightCrewMember().getAirline());
			super.state(isWrongAirlineLeg, "leg", "acme.validation.flightAssignment.leg.airline");

			boolean isLinkedToPastLeg = flightAssignment.getLeg().getScheduledDeparture().before(MomentHelper.getCurrentMoment());
			super.state(!isLinkedToPastLeg, "leg", "acme.validation.flightAssignment.leg.lastUpdate");
		}

		// Each leg can only have one pilot and one co-pilot
		if (flightAssignment.getDuty() != null && flightAssignment.getLeg() != null) {
			boolean isDutyAlreadyAssigned = this.repository.hasDutyAssigned(flightAssignment.getLeg().getId(), flightAssignment.getDuty(), flightAssignment.getId());
			super.state(!isDutyAlreadyAssigned, "duty", "acme.validation.flightAssignment.duty");
		}

	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		assert flightAssignment != null;

		flightAssignment.setDraftMode(false);
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		assert flightAssignment != null;

		Dataset dataset = super.unbindObject(flightAssignment, "duty", "lastUpdate", "status", "remarks", "draftMode", "flightCrewMember", "leg");

		dataset.put("flightCrewMember", flightAssignment.getFlightCrewMember().getIdentity().getFullName());

		// Duty choices
		SelectChoices dutyChoices = SelectChoices.from(DutyType.class, flightAssignment.getDuty());
		dataset.put("dutyChoices", dutyChoices);
		dataset.put("duty", dutyChoices.getSelected().getKey());

		// Status choices
		SelectChoices statusChoices = SelectChoices.from(AssignmentStatus.class, flightAssignment.getStatus());
		dataset.put("statusChoices", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());

		// Leg choices
		SelectChoices legChoices = SelectChoices.from(this.repository.findAllLegsByAirlineId(flightAssignment.getFlightCrewMember().getAirline().getId()), "flightNumber", flightAssignment.getLeg());
		dataset.put("legChoices", legChoices);
		dataset.put("leg", legChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
