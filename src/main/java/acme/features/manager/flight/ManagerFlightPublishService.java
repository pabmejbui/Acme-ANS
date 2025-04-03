
package acme.features.manager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.entities.flights.Leg;
import acme.realms.manager.Manager;

@GuiService
public class ManagerFlightPublishService extends AbstractGuiService<Manager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerFlightRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Flight flight;

		id = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(id);
		status = flight != null && super.getRequest().getPrincipal().hasRealm(flight.getManager());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findFlightById(id);
		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		super.bindObject(flight, "tag", "selfTransfer", "cost", "description");
	}

	@Override
	public void validate(final Flight flight) {
		// Solo permitir publicar si está en modo borrador
		super.state(flight.isDraftMode(), "*", "acme.validation.manager.flight.not-in-draft");

		// Validar que tenga al menos una escala
		int numberOfLegs = flight.getNumberOfLayovers();
		super.state(numberOfLegs > 0, "*", "acme.validation.manager.flight.publish.requires-legs");

		// Validar que todas las escalas estén publicadas
		Collection<Leg> legs = this.repository.findLegsByFlightId(flight.getId());
		boolean allPublished = legs.stream().allMatch(l -> !l.isDraftMode());
		super.state(allPublished, "*", "acme.validation.manager.flight.publish.all-legs-must-be-published");
	}

	@Override
	public void perform(final Flight flight) {
		flight.setDraftMode(false);
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset = super.unbindObject(flight, "tag", "selfTransfer", "cost", "description", "draftMode");
		super.getResponse().addData(dataset);
	}
}
