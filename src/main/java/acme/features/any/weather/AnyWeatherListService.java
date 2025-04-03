
package acme.features.any.weather;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.weather.WeatherCondition;

@GuiService
public class AnyWeatherListService extends AbstractGuiService<Any, WeatherCondition> {

	@Autowired
	private AnyWeatherRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<WeatherCondition> weatherConditions = this.repository.findAllWeatherConditions();
		super.getBuffer().addData(weatherConditions);
	}

	@Override
	public void unbind(final WeatherCondition object) {
		Dataset dataset = super.unbindObject(object, "city", "country", "temperature", "description", "reportTime");
		super.getResponse().addData(dataset);
	}
}
