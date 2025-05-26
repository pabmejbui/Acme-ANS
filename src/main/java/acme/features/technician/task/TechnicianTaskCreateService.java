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
public class TechnicianTaskCreateService extends AbstractGuiService<Technician, Task> {

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

		method = super.getRequest().getMethod();

		if (method.equals("GET"))
			authorised = true;
		else {
			int id;
			int version;

			taskType = super.getRequest().getData("type", String.class);
			correctType = "0".equals(taskType) || Arrays.stream(TaskType.values()).map(TaskType::name).anyMatch(name -> name.equals(taskType));

			id = super.getRequest().getData("id", int.class);
			version = super.getRequest().getData("version", int.class);

			authorised = id == 0 && version == 0 && correctType;
		}
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		Task task;
		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		task = new Task();
		task.setDraftMode(true);
		task.setTechnician(technician);

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
		this.repository.save(task);
	}

	@Override
	public void unbind(final Task task) {
		SelectChoices choices;

		Dataset dataset;
		choices = SelectChoices.from(TaskType.class, task.getType());

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration", "draftMode");

		dataset.put("type", choices.getSelected().getKey());
		dataset.put("type", choices);

		super.getResponse().addData(dataset);
	}

}