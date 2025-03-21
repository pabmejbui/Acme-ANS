
package acme.features.any.weather;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractRestController;
import acme.entities.weather.WeatherCondition;

@RestController
public class AnyWeatherRestController extends AbstractRestController<Any, WeatherCondition> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyWeatherListService listService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
	}

}
