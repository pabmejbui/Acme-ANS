
package acme.features.manager.leg;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Leg;
import acme.entities.flights.LegStatus;
import acme.realms.Manager;

@GuiService
public class ManagerLegShowService extends AbstractGuiService<Manager, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerLegRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Leg leg;
		Manager manager;

		masterId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(masterId);
		manager = leg == null ? null : leg.getFlight().getManager();
		status = super.getRequest().getPrincipal().hasRealm(manager) && leg != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;
		SelectChoices choices;

		choices = SelectChoices.from(LegStatus.class, leg.getStatus());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode");
		dataset.put("duration", leg.getDuration());
		dataset.put("statuses", choices);
		dataset.put("flight", leg.getFlight().getId());
		dataset.put("originAirport", leg.getOriginAirport().getCity());
		dataset.put("destinationAirport", leg.getDestinationAirport().getCity());
		dataset.put("aircraft", leg.getAircraft().getModel());
		dataset.put("airline", leg.getAirline().getIataCode());
		super.getResponse().addData(dataset);
	}

}
