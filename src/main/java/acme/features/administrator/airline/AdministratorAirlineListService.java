
package acme.features.administrator.airline;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airlines.Airline;

@GuiService
public class AdministratorAirlineListService extends AbstractGuiService<Administrator, Airline> {

	@Autowired
	private AdministratorAirlineRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Airline> airlines = this.repository.findAllAirlines();
		super.getBuffer().addData(airlines);
	}

	@Override
	public void unbind(final Airline airline) {
		Dataset dataset = super.unbindObject(airline, "name", "iataCode", "website", "type", "draftMode");
		super.getResponse().addData(dataset);
	}
}
