package acme.features.technician.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.tasks.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskListService extends AbstractGuiService<Technician, Task> {

	//Internal state ---------------------------------------------

	@Autowired
	private TechnicianTaskRepository repository;

	//AbstractGuiService interface -------------------------------


	@Override
	public void authorise() {
		boolean authorised = false;
		Principal principal = super.getRequest().getPrincipal();
		int userAccountId = principal.getAccountId();

		Technician technician = this.repository.findTechnicianByUserAccountId(userAccountId);

		if (technician != null)
			authorised = true;

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		Collection<Task> task;
		int technicianId;

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();

		task = this.repository.findTasksByTechnicianId(technicianId);
		super.getResponse().addGlobal("showCreate", true);
		super.getBuffer().addData(task);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;

		dataset = super.unbindObject(task, "type", "priority", "draftMode", "description", "estimatedDuration");

		super.getResponse().addGlobal("showCreate", true);
		super.addPayload(dataset, task, "type");

		super.getResponse().addData(dataset);
	}

}
