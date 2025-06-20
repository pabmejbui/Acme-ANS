
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flights.Flight;
import acme.entities.flights.Leg;
import acme.entities.flights.LegStatus;
import acme.features.manager.flight.ManagerFlightRepository;
import acme.realms.manager.Manager;

@GuiService
public class ManagerLegCreateService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository	lr;

	@Autowired
	private ManagerFlightRepository	fr;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		// ✅ Validar que venga el parámetro masterId (previene errores con int)
		super.state(super.getRequest().hasData("masterId"), "*", "acme.validation.manager.leg.invalid-request");

		int masterId = super.getRequest().getData("masterId", int.class);
		Flight flight = this.fr.findFlightById(masterId);
		Manager current = (Manager) super.getRequest().getPrincipal().getActiveRealm();

		super.state(flight != null, "*", "acme.validation.manager.leg.invalid-request");
		if (flight == null)
			return;

		super.state(flight.getManager().equals(current), "*", "acme.validation.manager.leg.not-owner");
		if (!flight.getManager().equals(current))
			return;

		super.state(flight.isDraftMode(), "*", "acme.validation.manager.leg.flight-not-in-draft");
		if (!flight.isDraftMode())
			return;

		Leg leg = new Leg();
		leg.setFlight(flight);
		leg.setFlightNumber("");
		leg.setStatus(LegStatus.ON_TIME);
		leg.setDraftMode(true);

		super.getBuffer().addData(leg);
		super.getResponse().addGlobal("allowCreate", true);
		super.getResponse().addGlobal("masterId", masterId);
	}

	@Override
	public void bind(final Leg leg) {
		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode", "originAirport", "destinationAirport", "aircraft", "airline");
	}

	@Override
	public void validate(final Leg leg) {
		if (!super.getBuffer().getErrors().hasErrors("destinationAirport") && !super.getBuffer().getErrors().hasErrors("originAirport")) {
			boolean sameAirport = leg.getOriginAirport().equals(leg.getDestinationAirport());
			super.state(!sameAirport, "destinationAirport", "acme.validation.manager.leg.departure-equals-arrival");
		}

		if (!super.getBuffer().getErrors().hasErrors("originAirport")) {
			int flightId = leg.getFlight().getId();
			Collection<Leg> existingLegs = this.fr.findLegsByFlightId(flightId);

			if (!existingLegs.isEmpty()) {
				Leg lastLeg = existingLegs.stream().max((l1, l2) -> l1.getScheduledDeparture().compareTo(l2.getScheduledDeparture())).orElse(null);

				boolean isConnected = lastLeg != null && lastLeg.getDestinationAirport().equals(leg.getOriginAirport());
				super.state(isConnected, "originAirport", "acme.validation.manager.leg.not-connected-to-previous");
			}
		}

	}

	@Override
	public void perform(final Leg leg) {
		super.state(leg != null, "*", "acme.validation.manager.leg.invalid-request");
		super.state(leg.getFlight() != null, "*", "acme.validation.manager.leg.invalid-flight");
		this.lr.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;
		Collection<Airport> airports = this.lr.findAllAirports();
		Collection<Aircraft> aircrafts = this.lr.findAllAircrafts();

		SelectChoices statuses = SelectChoices.from(LegStatus.class, leg.getStatus());
		SelectChoices originChoices = SelectChoices.from(airports, "city", leg.getOriginAirport());
		SelectChoices destinationChoices = SelectChoices.from(airports, "city", leg.getDestinationAirport());
		SelectChoices aircraftChoices = SelectChoices.from(aircrafts, "model", leg.getAircraft());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode");

		dataset.put("statuses", statuses);
		dataset.put("flightId", leg.getFlight().getId());
		dataset.put("masterId", leg.getFlight().getId());
		dataset.put("flightTag", leg.getFlight().getTag());

		dataset.put("originAirport", leg.getOriginAirport() != null ? leg.getOriginAirport().getCity() : null);
		dataset.put("destinationAirport", leg.getDestinationAirport() != null ? leg.getDestinationAirport().getCity() : null);
		dataset.put("aircraft", leg.getAircraft() != null ? leg.getAircraft().getModel() : null);

		dataset.put("originAirports", originChoices);
		dataset.put("destinationAirports", destinationChoices);
		dataset.put("aircrafts", aircraftChoices);

		super.getResponse().addData(dataset);
	}
}
