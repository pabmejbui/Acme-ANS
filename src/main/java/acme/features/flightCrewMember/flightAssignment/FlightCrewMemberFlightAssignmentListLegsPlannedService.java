
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentListLegsPlannedService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		Collection<FlightAssignment> plannedFlightAssignments = this.repository.findAllPlannedFlightAssignments(MomentHelper.getCurrentMoment(), flightCrewMember.getId());

		super.getBuffer().addData(plannedFlightAssignments);
	}

	@Override
	public void unbind(final FlightAssignment plannedFlightAssignments) {
		assert plannedFlightAssignments != null;

		Dataset dataset = super.unbindObject(plannedFlightAssignments, "duty", "lastUpdate", "status", "remarks", "draftMode", "leg");

		dataset.put("leg", plannedFlightAssignments.getLeg().getFlightNumber());

		super.addPayload(dataset, plannedFlightAssignments, "duty", "lastUpdate", "status", "remarks", "draftMode", "leg");
		super.getResponse().addData(dataset);

	}
}
