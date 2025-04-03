
package acme.features.authenticated.manager;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.manager.Manager;

@GuiService
public class AuthenticatedManagerUpdateService extends AbstractGuiService<Authenticated, Manager> {

	@Autowired
	private AuthenticatedManagerRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(Manager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int userAccountId = super.getRequest().getPrincipal().getAccountId();
		Manager object = this.repository.findOneManagerByUserAccountId(userAccountId);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Manager object) {
		assert object != null;

		super.bindObject(object, "identifier", "experience", "birthDate", "pictureLink");
	}

	@Override
	public void validate(final Manager object) {
		assert object != null;
	}

	@Override
	public void perform(final Manager object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Manager object) {
		Dataset dataset;

		dataset = super.unbindObject(object, "identifier", "experience", "birthDate", "pictureLink");

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
