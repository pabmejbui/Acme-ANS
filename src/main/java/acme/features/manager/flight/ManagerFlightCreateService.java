
package acme.features.manager.flight;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.configuration.SystemConfiguration;
import acme.entities.flights.Flight;
import acme.realms.manager.Manager;

@GuiService
public class ManagerFlightCreateService extends AbstractGuiService<Manager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerFlightRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Flight flight;
		Manager manager;

		manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();

		flight = new Flight();
		flight.setDraftMode(true);
		flight.setManager(manager);

		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		super.bindObject(flight, "tag", "selfTransfer", "cost", "description");
	}

	@Override
	public void validate(final Flight flight) {

		if (!super.getBuffer().getErrors().hasErrors("cost")) {
			super.state(flight.getCost().getAmount() > 0, "cost", "manager.flight.form.error.cost-must-be-positive");
			List<SystemConfiguration> conf = this.repository.findSystemConfiguration();
			final boolean foundCurrency = Stream.of(conf.get(0).getAcceptedCurrencies().split(",")).anyMatch(c -> c.equals(flight.getCost().getCurrency()));

			super.state(foundCurrency, "cost", "manager.flight.form.error.currency-not-supported");
		}

	}

	@Override
	public void perform(final Flight flight) {
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "selfTransfer", "cost", "description", "draftMode");
		dataset.put("scheduledDeparture", flight.getScheduledDeparture());
		dataset.put("scheduledArrival", flight.getScheduledArrival());
		dataset.put("originCity", flight.getOriginCity());
		dataset.put("destinationCity", flight.getDestinationCity());
		dataset.put("numberOfLayovers", flight.getNumberOfLayovers());
		super.getResponse().addData(dataset);
	}

}
