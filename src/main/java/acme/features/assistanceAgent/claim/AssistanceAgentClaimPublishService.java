
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
public class AssistanceAgentClaimPublishService extends AbstractGuiService<AssistanceAgent, Claim> {

	//Internal state ---------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository repository;

	//AbstractGuiService interface -------------------------------


	@Override
	public void authorise() {
		boolean status;
		int assistanceAgentId;
		int claimId;
		AssistanceAgent assistanceAgent;
		Claim selectedClaim;

		assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		assistanceAgent = this.repository.findAssistanceAgentById(assistanceAgentId);
		claimId = super.getRequest().getData("id", int.class);
		selectedClaim = this.repository.findClaimById(claimId);

		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class) && selectedClaim.getAssistanceAgent().equals(assistanceAgent);

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		int claimId;
		Claim claim;

		claimId = super.getRequest().getData("id", int.class);

		claim = this.repository.findClaimById(claimId);
		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {

		super.bindObject(claim, "passengerEmail", "description", "type", "leg");
	}

	@Override
	public void validate(final Claim claim) {
		;
	}

	@Override
	public void perform(final Claim claim) {
		claim.setDraftMode(true);
		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Collection<Leg> legs;
		SelectChoices typesChoices;
		SelectChoices legsChoices;
		Dataset dataset;

		legs = this.repository.findAllLeg();
		typesChoices = SelectChoices.from(ClaimType.class, claim.getType());
		legsChoices = SelectChoices.from(legs, "flightNumber", claim.getLeg());
		ClaimIndicator indicator = claim.getIndicator();

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "draftMode");
		dataset.put("types", typesChoices);
		dataset.put("legs", legsChoices);
		dataset.put("indicator", indicator);
		dataset.put("leg", legsChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
