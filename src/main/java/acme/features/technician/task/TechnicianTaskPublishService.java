package acme.features.technician.task;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.tasks.Task;
import acme.entities.tasks.TaskType;
import acme.realms.technician.Technician;

@GuiService
public class TechnicianTaskPublishService extends AbstractGuiService<Technician, Task> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository repository;


	// AbstractGuiService interface -------------------------------------------
	
	@Override
	public void authorise() {
		String method;
		boolean correctType;
		boolean authorised = false;
		String taskType;

		int id = super.getRequest().getData("id", int.class);
		Task task = this.repository.findTaskById(id);
		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		if (task != null) {
			method = super.getRequest().getMethod();

			if (method.equals("GET"))
				authorised = task.getTechnician().equals(technician) && task.getDraftMode();
			else {

				taskType = super.getRequest().getData("type", String.class);
				correctType = "0".equals(taskType) || Arrays.stream(TaskType.values()).map(TaskType::name).anyMatch(name -> name.equals(taskType));

				authorised = correctType;
			}
		}
		super.getResponse().setAuthorised(authorised);
	}
	
	@Override
	public void load() {
		Task task;
		int id;

		id = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(id);

		super.getBuffer().addData(task);
	}

	@Override
	public void bind(final Task task) {
		super.bindObject(task, "type", "description", "priority", "estimatedDuration");
	}

	@Override
	public void validate(final Task task) {
	}

	@Override
	public void perform(final Task task) {
		task.setDraftMode(false);
		this.repository.save(task);
	}
}
