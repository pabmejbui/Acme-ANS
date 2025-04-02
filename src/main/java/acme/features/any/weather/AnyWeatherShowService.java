
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
		int id = super.getRequest().getData("id", int.class);
		WeatherCondition condition = this.repository.findOneWeatherConditionById(id);
		super.getResponse().setAuthorised(condition != null);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		WeatherCondition condition = this.repository.findOneWeatherConditionById(id);
		super.getBuffer().addData(condition);
	}

	@Override
	public void unbind(final WeatherCondition condition) {
		Dataset dataset = super.unbindObject(condition, "city", "country", "temperature", "humidity", "windSpeed", "description", "reportTime");
		super.getResponse().addData(dataset);
	}
}
