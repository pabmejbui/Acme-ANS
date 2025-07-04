
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.flights.Leg;
import acme.realms.manager.Manager;

@GuiService
public class ManagerLegDeleteService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository repository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(id);
		boolean authorised = leg != null && leg.getFlight().getManager().equals(super.getRequest().getPrincipal().getActiveRealm());

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(id);
		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
	}

	@Override
	public void validate(final Leg leg) {
		super.state(leg.isDraftMode(), "*", "acme.validation.manager.leg.leg-not-in-draft");

		Collection<Claim> claims = this.repository.findClaimsByLegId(leg.getId());
		super.state(claims.isEmpty(), "*", "acme.validation.manager.leg.delete.has-claims");

	}

	@Override
	public void perform(final Leg leg) {
		this.repository.delete(leg);
	}

	@Override
	public void unbind(final Leg leg) {
	}
}
