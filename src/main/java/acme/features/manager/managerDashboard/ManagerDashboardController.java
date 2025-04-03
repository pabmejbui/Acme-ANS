
package acme.features.manager.managerDashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.forms.ManagerDashboard;
import acme.realms.Manager;

@GuiController
public class ManagerDashboardController extends AbstractGuiController<Manager, ManagerDashboard> {

	@Autowired
	protected ManagerDashboardShowService showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
	}
}
