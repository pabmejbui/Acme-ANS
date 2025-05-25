
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentListMyLegsPlannedService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<FlightAssignment> assignments;
		Date now;
		int memberId;

		now = MomentHelper.getCurrentMoment();
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();

		assignments = this.repository.findMyPlannedAssignments(now, memberId);

		super.getBuffer().addData(assignments);

	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;

		dataset = super.unbindObject(assignment, "lastUpdate", "status", "duty");
		super.addPayload(dataset, assignment, "remarks");

		super.getResponse().addData(dataset);
	}
}
