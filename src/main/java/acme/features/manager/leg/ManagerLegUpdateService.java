
package acme.features.manager.leg;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Leg;
import acme.realms.manager.Manager;

@GuiService
public class ManagerLegUpdateService extends AbstractGuiService<Manager, Leg> {

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
		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode", "originAirport", "destinationAirport", "aircraft");
	}

	@Override
	public void validate(final Leg leg) {
		super.state(leg.isDraftMode(), "*", "acme.validation.manager.leg.leg-not-in-draft");
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode", "originAirport", "destinationAirport", "aircraft");
		dataset.put("id", leg.getId());
		super.getResponse().addData(dataset);
	}
}
