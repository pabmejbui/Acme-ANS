
package acme.features.customer.recommendationDashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.forms.RecommendationDashboard;
import acme.realms.customer.Customer;

@GuiController
public class CustomerRecommendationDashboardController extends AbstractGuiController<Customer, RecommendationDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerRecommendationDashboardListService listService;
	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
	}

}
