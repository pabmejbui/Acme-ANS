
package acme.features.any.weather;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.weather.WeatherCondition;

@GuiService
public class AnyWeatherShowService extends AbstractGuiService<Any, WeatherCondition> {

	@Autowired
	private AnyWeatherRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		WeatherCondition weatherCondition;
		int id;

		id = super.getRequest().getData("id", int.class);
		weatherCondition = this.repository.findWeatherConditionById(id);

		super.getBuffer().addData(weatherCondition);
	}

	@Override
	public void unbind(final WeatherCondition weatherCondition) {
		Dataset dataset;
		dataset = super.unbindObject(weatherCondition, "city", "country", "temperature", "humidity", "windSpeed", "description", "reportTime");
		super.getResponse().addData(dataset);
	}
}
