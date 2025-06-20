
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flights.Leg;
import acme.entities.flights.LegStatus;
import acme.realms.manager.Manager;

@GuiService
public class ManagerLegShowService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository lr;


	@Override
	public void authorise() {
		boolean status;
		int legId;
		Leg leg;

		legId = super.getRequest().getData("id", int.class);
		leg = this.lr.findLegById(legId);

		status = leg != null && super.getRequest().getPrincipal().hasRealm(leg.getFlight().getManager());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Leg leg = this.lr.findLegById(id);

		super.state(leg != null, "*", "acme.validation.manager.leg.invalid-request");
		if (leg == null)
			return;

		super.getBuffer().addData(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Collection<Airport> airports = this.lr.findAllAirports();
		Collection<Aircraft> aircrafts = this.lr.findAllAircrafts();

		SelectChoices statuses = SelectChoices.from(LegStatus.class, leg.getStatus());
		SelectChoices originChoices = SelectChoices.from(airports, "name", leg.getOriginAirport());
		SelectChoices destinationChoices = SelectChoices.from(airports, "name", leg.getDestinationAirport());
		SelectChoices aircraftChoices = SelectChoices.from(aircrafts, "model", leg.getAircraft());

		Dataset dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode");

		dataset.put("duration", leg.getDuration());
		dataset.put("flightTag", leg.getFlight().getTag());

		dataset.put("status", statuses.getSelected().getKey());
		dataset.put("statuses", statuses);

		dataset.put("originAirport", originChoices.getSelected().getKey());
		dataset.put("originAirports", originChoices);

		dataset.put("destinationAirport", destinationChoices.getSelected().getKey());
		dataset.put("destinationAirports", destinationChoices);

		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);

		dataset.put("masterId", leg.getFlight().getId());
		dataset.put("allowUpdate", leg.isDraftMode());
		dataset.put("allowDelete", leg.isDraftMode());
		dataset.put("allowPublish", leg.isDraftMode());

		super.getResponse().addData(dataset);
	}
}
