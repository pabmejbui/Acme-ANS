
package acme.features.any.flight;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractGuiController;
import acme.entities.flights.Flight;

@Controller
public class AnyFlightController extends AbstractGuiController<Any, Flight> {

	@Autowired
	private AnyFlightListService	listService;

	@Autowired
	private AnyFlightShowService	showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}

}
