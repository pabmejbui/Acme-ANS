
package acme.features.any.weather;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.weather.WeatherCondition;

@GuiService
public class AnyWeatherShowService extends AbstractGuiService<Any, WeatherCondition> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyWeatherRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		WeatherCondition weatherCondition;

		id = super.getRequest().getData("id", int.class);
		weatherCondition = this.repository.findWeatherConditionById(id);
		status = weatherCondition != null;

		super.getResponse().setAuthorised(status);
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
