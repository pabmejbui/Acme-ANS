
package acme.features.customer.customerDashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.forms.CustomerDashboard;
import acme.realms.customer.Customer;

@GuiController
public class CustomerDashboardController extends AbstractGuiController<Customer, CustomerDashboard> {

	@Autowired
	protected CustomerDashboardShowService showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
	}

}
