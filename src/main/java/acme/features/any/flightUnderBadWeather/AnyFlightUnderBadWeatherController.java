
package acme.features.any.flightUnderBadWeather;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flights.Flight;

@GuiController
public class AnyFlightUnderBadWeatherController extends AbstractGuiController<Any, Flight> {

	@Autowired
	protected AnyFlightUnderBadWeatherListService listService;


	@PostConstruct
	protected void initialise() {
		super.addCustomCommand("list-under-bad-weather", "list", this.listService);
	}
}
