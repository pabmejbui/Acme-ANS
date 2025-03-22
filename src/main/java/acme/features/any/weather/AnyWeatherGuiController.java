
package acme.features.any.weather;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.weather.WeatherCondition;

@GuiController
public class AnyWeatherGuiController extends AbstractGuiController<Any, WeatherCondition> {

	@Autowired
	private AnyWeatherListService	weatherListService;

	@Autowired
	private AnyWeatherShowService	weatherShowService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.weatherListService);
		super.addBasicCommand("show", this.weatherShowService);
	}
}
