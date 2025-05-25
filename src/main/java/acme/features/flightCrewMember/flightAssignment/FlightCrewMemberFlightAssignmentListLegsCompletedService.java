
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
public class FlightCrewMemberFlightAssignmentListLegsCompletedService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

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
		Collection<FlightAssignment> completedFlightAssignments = this.repository.findAllCompletedFlightAssignments(MomentHelper.getCurrentlastUpdate(), flightCrewMember.getId());

		super.getBuffer().addData(completedFlightAssignments);
	}

	@Override
	public void unbind(final FlightAssignment completedFlightAssignments) {
		assert completedFlightAssignments != null;

		Dataset dataset = super.unbindObject(completedFlightAssignments, "duty", "lastUpdate", "status", "remarks", "draftMode", "leg");

		dataset.put("leg", completedFlightAssignments.getLeg().getFlightNumber());

		super.addPayload(dataset, completedFlightAssignments, "duty", "lastUpdate", "status", "remarks", "draftMode", "leg");
		super.getResponse().addData(dataset);

	}
}
