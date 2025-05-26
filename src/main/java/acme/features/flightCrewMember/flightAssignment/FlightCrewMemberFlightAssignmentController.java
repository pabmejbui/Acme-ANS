
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
	private FlightCrewMemberFlightAssignmentListMyLegsCompletedService	listMyCompletedService;

	@Autowired
	private FlightCrewMemberFlightAssignmentListLegsPlannedService		listPlannedService;

	@Autowired
	private FlightCrewMemberFlightAssignmentListMyLegsPlannedService	listMyPlannedService;

	@Autowired
	private FlightCrewMemberFlightAssignmentShowService					showService;

	@Autowired
	private FlightCrewMemberFlightAssignmentCreateService				createService;

	@Autowired
	private FlightCrewMemberFlightAssignmentUpdateService				updateService;

	@Autowired
	private FlightCrewMemberFlightAssignmentPublishService				publishService;

	@Autowired
	private FlightCrewMemberFlightAssignmentDeleteService				deleteService;


	@PostConstruct
	protected void initialise() {
		super.addCustomCommand("list-completed", "list", this.listCompletedService);
		super.addCustomCommand("list-mine-completed", "list", this.listMyCompletedService);
		super.addCustomCommand("list-planned", "list", this.listPlannedService);
		super.addCustomCommand("list-mine-planned", "list", this.listMyPlannedService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addCustomCommand("publish", "update", this.publishService);
	}
}
