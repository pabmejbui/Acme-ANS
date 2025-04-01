
package acme.features.flightCrewMember.flightAssignment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiController
public class FlightCrewMemberFlightAssignmentController extends AbstractGuiController<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentListLegsCompletedService	listCompletedService;

	@Autowired
	private FlightCrewMemberFlightAssignmentListLegsPlannedService		listPlannedService;

	@Autowired
	private FlightCrewMemberFlightAssignmentShowService					showService;

	@Autowired
	private FlightCrewMemberFlightAssignmentCreateService				createService;

	@Autowired
	private FlightCrewMemberFlightAssignmentUpdateService				updateService;

	@Autowired
	private FlightCrewMemberFlightAssignmentPublishService				publishService;


	@PostConstruct
	protected void initialise() {
		super.addCustomCommand("list-completed", "list", this.listCompletedService);
		super.addCustomCommand("list-planned", "list", this.listPlannedService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addCustomCommand("publish", "update", this.publishService);
	}
}
