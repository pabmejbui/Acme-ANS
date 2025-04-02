
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimIndicator;
import acme.entities.claims.ClaimType;
import acme.entities.flights.Leg;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimShowService extends AbstractGuiService<AssistanceAgent, Claim> {

	//Internal state ---------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository repository;

	//AbstractGuiService interface -------------------------------


	@Override
	public void authorise() {
		boolean status;
		int claimAgentId;
		int claimId;
		int agentId;
		Claim claim;

		claimId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(claimId);
		claimAgentId = super.getRequest().getPrincipal().getAccountId();
		agentId = claim.getAssistanceAgent().getUserAccount().getId();

		status = claimAgentId == agentId;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Claim claim;
		int claimId;

		claimId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(claimId);

		super.getBuffer().addData(claim);

	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		ClaimIndicator indicator;
		SelectChoices typesChoices;
		SelectChoices legsChoices;

		Collection<Leg> legs;

		indicator = claim.getIndicator();

		typesChoices = SelectChoices.from(ClaimType.class, claim.getType());
		legs = this.repository.findAllLeg();
		legsChoices = SelectChoices.from(legs, "flightNumber", claim.getLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "publish");
		dataset.put("types", typesChoices);
		dataset.put("indicator", indicator);
		dataset.put("legs", legsChoices);
		dataset.put("leg", legsChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
