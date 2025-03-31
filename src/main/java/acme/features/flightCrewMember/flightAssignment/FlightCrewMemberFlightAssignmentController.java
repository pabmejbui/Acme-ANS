
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
	private FlightCrewMemberFlightAssignmentListService		listService;

	@Autowired
	private FlightCrewMemberFlightAssignmentShowService		showService;

	//	@Autowired
	//	private FlightCrewMemberFlightAssignmentDeleteService	deleteService;
	//
	@Autowired
	private FlightCrewMemberFlightAssignmentCreateService	createService;
	//
	//	@Autowired
	//	private FlightCrewMemberFlightAssignmentUpdateService	updateService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		//				super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("create", this.createService);
		//		super.addBasicCommand("update", this.updateService);
	}
}
