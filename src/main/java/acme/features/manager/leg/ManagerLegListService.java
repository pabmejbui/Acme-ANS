
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.entities.flights.Leg;
import acme.features.manager.flight.ManagerFlightRepository;
import acme.realms.manager.Manager;

@GuiService
public class ManagerLegListService extends AbstractGuiService<Manager, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerFlightRepository fr;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int flightId;
		Flight flight;

		flightId = super.getRequest().getData("flightId", int.class);
		flight = this.fr.findFlightById(flightId);

		status = flight != null && super.getRequest().getPrincipal().hasRealm(flight.getManager());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Leg> legs;
		int flightId;

		flightId = super.getRequest().getData("flightId", int.class);
		legs = this.fr.findLegsByFlightId(flightId);

		super.getBuffer().addData(legs);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status");
		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<Leg> legs) {
		int flightId;
		Flight flight;
		boolean showCreate;

		flightId = super.getRequest().getData("flightId", int.class);
		flight = this.fr.findFlightById(flightId);

		showCreate = flight != null && flight.isDraftMode() && super.getRequest().getPrincipal().hasRealm(flight.getManager());

		super.getResponse().addGlobal("flightId", flightId);
		super.getResponse().addGlobal("showCreate", showCreate);
	}
}
