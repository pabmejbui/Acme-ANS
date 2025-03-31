
package acme.features.administrator.service;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.services.Service;

@GuiService
public class AdministratorServiceShowService extends AbstractGuiService<Administrator, Service> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorServiceRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		boolean exists = this.repository.findServiceById(id) != null;
		super.getResponse().setAuthorised(exists);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Service service = this.repository.findServiceById(id);
		super.getBuffer().addData(service);
	}

	@Override
	public void unbind(final Service service) {
		Dataset dataset = super.unbindObject(service, "name", "pictureUrl", "averageDwellTime", "promotionCode", "discountAmount");
		super.getResponse().addData(dataset);
	}
}
