
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
		Collection<WeatherCondition> weatherConditions;

		weatherConditions = this.repository.findAllWeatherConditions();

		System.out.println("✅ Servicio AnyWeatherListService ejecutado");
		System.out.println("🔍 Se encontraron " + weatherConditions.size() + " registros de WeatherCondition");

		for (WeatherCondition wc : weatherConditions)
			System.out.println("🌍 Ciudad: " + wc.getCity() + ", Temperatura: " + wc.getTemperature());

		super.getBuffer().addData(weatherConditions);
	}

	@Override
	public void unbind(final WeatherCondition weatherCondition) {
		Dataset dataset;

		dataset = super.unbindObject(weatherCondition, "city", "country", "temperature", "humidity", "windSpeed", "description", "reportTime");
		super.getResponse().addData(dataset);
	}

}
