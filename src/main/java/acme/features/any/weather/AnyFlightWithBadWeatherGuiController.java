
package acme.features.any.weather;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flights.Flight;

@GuiController
public class AnyFlightWithBadWeatherGuiController extends AbstractGuiController<Any, Flight> {

	@Autowired
	private AnyFlightWithBadWeatherListService flightWithBadWeatherListService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.flightWithBadWeatherListService);
	}
}
