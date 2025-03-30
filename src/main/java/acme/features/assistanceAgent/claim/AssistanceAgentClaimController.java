
package acme.features.assistanceAgent.claim;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.claims.Claim;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiController
public class AssistanceAgentClaimController extends AbstractGuiController<AssistanceAgent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimListService		listService;

	@Autowired
	private AssistanceAgentClaimShowService		showService;

	@Autowired
	private AssistanceAgentClaimCreateService	createService;

	/*
	 * @Autowired
	 * private AssistanceAgentClaimUpdateService updateService;
	 * 
	 * @Autowired
	 * private AssistanceAgentClaimPublishService publishService;
	 * 
	 * @Autowired
	 * private AssistanceAgentClaimDeleteService deleteService;
	 */

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);

	}
}
