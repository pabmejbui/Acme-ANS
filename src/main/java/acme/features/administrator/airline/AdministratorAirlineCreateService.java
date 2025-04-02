
package acme.features.administrator.airline;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airlines.Airline;
import acme.entities.airlines.AirlineType;

@GuiService
public class AdministratorAirlineCreateService extends AbstractGuiService<Administrator, Airline> {

	@Autowired
	private AdministratorAirlineRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Airline airline = new Airline();
		airline.setFoundationMoment(MomentHelper.getCurrentMoment());
		airline.setDraftMode(true);
		super.getBuffer().addData(airline);
	}

	@Override
	public void bind(final Airline airline) {
		super.bindObject(airline, "name", "iataCode", "website", "type", "foundationMoment", "optionalEmail", "phoneNumber", "draftMode");
	}

	@Override
	public void validate(final Airline airline) {
		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Airline airline) {
		this.repository.save(airline);
	}

	@Override
	public void unbind(final Airline airline) {
		Dataset dataset = super.unbindObject(airline, "name", "iataCode", "website", "type", "foundationMoment", "optionalEmail", "phoneNumber", "draftMode");

		dataset.put("confirmation", false);

		SelectChoices types = SelectChoices.from(AirlineType.class, airline.getType());

		dataset.put("types", types);
		dataset.put("type", types.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
