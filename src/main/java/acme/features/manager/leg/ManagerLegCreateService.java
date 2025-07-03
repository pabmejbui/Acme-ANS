
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
		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode", "originAirport", "destinationAirport", "aircraft");
	}

	@Override
	public void validate(final Leg leg) {
		super.state(leg.getAircraft() != null, "aircraft", "acme.validation.manager.leg.aircraft-required");
		super.state(leg.getOriginAirport() != null, "originAirport", "acme.validation.manager.leg.origin-required");
		super.state(leg.getDestinationAirport() != null, "destinationAirport", "acme.validation.manager.leg.destination-required");
		super.state(leg.getFlightNumber() != null && !leg.getFlightNumber().trim().isEmpty(), "flightNumber", "acme.validation.manager.leg.flight-number-required");

		if (!super.getBuffer().getErrors().hasErrors("flightNumber")) {
			boolean validFormat = leg.getFlightNumber().matches("^[A-Z]{3}\\d{4}$");
			super.state(validFormat, "flightNumber", "acme.validation.manager.leg.flight-number.bad-format");
		}

		if (!super.getBuffer().getErrors().hasErrors("flightNumber") && leg.getAircraft() != null && leg.getAircraft().getAirline() != null) {
			String iata = leg.getAircraft().getAirline().getIataCode();
			super.state(leg.getFlightNumber().startsWith(iata), "flightNumber", "acme.validation.manager.leg.flight-number.iata-mismatch");
		}

		if (leg.getOriginAirport() != null && leg.getDestinationAirport() != null) {
			boolean sameAirport = leg.getOriginAirport().equals(leg.getDestinationAirport());
			super.state(!sameAirport, "destinationAirport", "acme.validation.manager.leg.departure-equals-arrival");
		}

		if (leg.getScheduledDeparture() != null && leg.getScheduledArrival() != null) {
			boolean isArrivalAfterDeparture = leg.getScheduledArrival().after(leg.getScheduledDeparture());
			super.state(isArrivalAfterDeparture, "scheduledArrival", "acme.validation.manager.leg.arrival-before-departure");

			long duration = leg.getScheduledArrival().getTime() - leg.getScheduledDeparture().getTime();
			super.state(duration >= 60000, "scheduledArrival", "acme.validation.manager.leg.valid-leg.time-conflict");
			super.state(duration <= 43200000L, "scheduledArrival", "acme.validation.manager.leg.valid-leg.unrealistic-duration");
		}

		if (leg.getFlight() != null && leg.getScheduledDeparture() != null && leg.getScheduledArrival() != null) {
			int flightId = leg.getFlight().getId();
			Collection<Leg> legs = this.lr.findLegsByFlightId(flightId);
			Collection<Leg> otherLegs = legs.stream().filter(l -> l.getId() != leg.getId()).toList();

			for (Leg other : otherLegs)
				if (other.getScheduledDeparture() != null && other.getScheduledArrival() != null) {
					boolean overlaps = !(leg.getScheduledArrival().before(other.getScheduledDeparture()) || leg.getScheduledDeparture().after(other.getScheduledArrival()));
					super.state(!overlaps, "*", "acme.validation.manager.leg.valid-leg.overlap");
				}

			if (!otherLegs.isEmpty()) {
				Leg previous = otherLegs.stream().filter(l -> l.getScheduledArrival() != null && l.getScheduledArrival().before(leg.getScheduledDeparture())).max((l1, l2) -> l1.getScheduledArrival().compareTo(l2.getScheduledArrival())).orElse(null);
				Leg next = otherLegs.stream().filter(l -> l.getScheduledDeparture() != null && l.getScheduledDeparture().after(leg.getScheduledArrival())).min((l1, l2) -> l1.getScheduledDeparture().compareTo(l2.getScheduledDeparture())).orElse(null);

				if (previous != null && previous.getDestinationAirport() != null && leg.getOriginAirport() != null) {
					boolean connected = previous.getDestinationAirport().equals(leg.getOriginAirport());
					super.state(connected, "originAirport", "acme.validation.manager.leg.not-connected-to-previous");
				}

				if (next != null && next.getOriginAirport() != null && leg.getDestinationAirport() != null) {
					boolean connected = leg.getDestinationAirport().equals(next.getOriginAirport());
					super.state(connected, "destinationAirport", "acme.validation.manager.leg.not-connected-to-next");
				}
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

		SelectChoices originChoices = new SelectChoices();
		originChoices.add("0", "----", leg.getOriginAirport() == null);
		for (Airport a : airports)
			originChoices.add(Integer.toString(a.getId()), a.getCity(), a.equals(leg.getOriginAirport()));

		SelectChoices destinationChoices = new SelectChoices();
		destinationChoices.add("0", "----", leg.getDestinationAirport() == null);
		for (Airport a : airports)
			destinationChoices.add(Integer.toString(a.getId()), a.getCity(), a.equals(leg.getDestinationAirport()));

		SelectChoices aircraftChoices = new SelectChoices();
		aircraftChoices.add("0", "----", leg.getAircraft() == null);
		for (Aircraft a : aircrafts)
			aircraftChoices.add(Integer.toString(a.getId()), a.getModel(), a.equals(leg.getAircraft()));

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode");

		dataset.put("statuses", statuses);
		dataset.put("flightId", leg.getFlight().getId());
		dataset.put("masterId", leg.getFlight().getId());
		dataset.put("flightTag", leg.getFlight().getTag());

		dataset.put("originAirport", leg.getOriginAirport() != null ? leg.getOriginAirport().getId() : 0);
		dataset.put("destinationAirport", leg.getDestinationAirport() != null ? leg.getDestinationAirport().getId() : 0);
		dataset.put("aircraft", leg.getAircraft() != null ? leg.getAircraft().getId() : 0);

		dataset.put("originAirports", originChoices);
		dataset.put("destinationAirports", destinationChoices);
		dataset.put("aircrafts", aircraftChoices);

		super.getResponse().addData(dataset);
	}
}
