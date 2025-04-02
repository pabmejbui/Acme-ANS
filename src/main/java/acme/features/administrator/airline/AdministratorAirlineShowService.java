
package acme.features.administrator.airline;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airlines.Airline;
import acme.entities.airlines.AirlineType;

@GuiService
public class AdministratorAirlineShowService extends AbstractGuiService<Administrator, Airline> {

	@Autowired
	private AdministratorAirlineRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Airline airline = this.repository.findAirlineById(id);
		super.getBuffer().addData(airline);
	}

	@Override
	public void unbind(final Airline airline) {
		Dataset dataset;
		SelectChoices types;

		types = SelectChoices.from(AirlineType.class, airline.getType());

		dataset = super.unbindObject(airline, "name", "iataCode", "website", "type", "foundationMoment", "optionalEmail", "phoneNumber", "draftMode");

		dataset.put("type", types.getSelected().getKey());
		dataset.put("types", types);

		super.getResponse().addData(dataset);
	}

}
