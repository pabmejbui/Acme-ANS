
package acme.features.assistanceAgent.claim;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimIndicator;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimListUndergoingService extends AbstractGuiService<AssistanceAgent, Claim> {

	//Internal state ---------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository repository;

	//AbstractGuiService interface -------------------------------


	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Claim> claims;
		Collection<Claim> claimsCompleted = new ArrayList<>();
		int assistanceAgentId;

		assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		claims = this.repository.findAllClaimsByAssistanceAgentId(assistanceAgentId);

		for (Claim cl : claims)
			if (cl.getIndicator() == ClaimIndicator.PENDING)
				claimsCompleted.add(cl);

		super.getBuffer().addData(claimsCompleted);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		ClaimIndicator indicator = claim.getIndicator();

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "draftMode");
		dataset.put("indicator", indicator);

		super.getResponse().addData(dataset);
	}
}
