
package acme.features.customer.recommendationDashboard;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.recommendation.Recommendation;
import acme.forms.RecommendationDashboard;
import acme.realms.customer.Customer;

@GuiService
public class CustomerRecommendationDashboardListService extends AbstractGuiService<Customer, RecommendationDashboard> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerRecommendationDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		List<RecommendationDashboard> recommendationDashboards = new LinkedList<>();
		String city = super.getRequest().getData("city", String.class);

		System.out.println("CIUDAD" + city);

		Collection<Recommendation> recommendations = this.repository.getRecommendationsOf(city);

		if (recommendations == null || recommendations.isEmpty()) {
			// Usamos addGlobal para añadir un mensaje de error en la respuesta
			super.getResponse().addGlobal("error", "No recommendations found for the specified city.");
			return;
		}

		// Mapeamos las recomendaciones a RecommendationDashboard.
		for (Recommendation recommendation : recommendations) {
			RecommendationDashboard dashboard = new RecommendationDashboard();
			dashboard.setCity(recommendation.getCity());
			dashboard.setName(recommendation.getName());
			dashboard.setRating(recommendation.getRating());
			recommendationDashboards.add(dashboard);
		}

		// Añadimos las recomendaciones al buffer para que se puedan mostrar en la vista.
		super.getBuffer().addData(recommendationDashboards);
	}

	@Override
	public void unbind(final RecommendationDashboard recommendationsDashboard) {
		// Desvinculamos los datos de cada recomendación y los añadimos a la respuesta.
		Dataset dataset = super.unbindObject(recommendationsDashboard, "city", "name", "rating", "type");
		super.getResponse().addData(dataset);
	}

}
