package acme.features.technician.maintenanceRecordTask;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceRecords.MaintenanceRecordTask;
import acme.entities.tasks.Task;
import acme.realms.technician.Technician;

@GuiService
public class TechnicianMaintenanceRecordTaskDeleteService extends AbstractGuiService<Technician, MaintenanceRecordTask> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordTaskRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean statusTask = true;
		boolean status = false;
		int taskId;
		Task task;
		int maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;
		Collection<Task> tasks;

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		tasks = this.repository.findValidTasksToUnlink(maintenanceRecord);

		if (super.getRequest().hasData("task", int.class)) {
			taskId = super.getRequest().getData("task", int.class);
			task = this.repository.findTaskById(taskId);

			if (!tasks.contains(task) && taskId != 0)
				statusTask = false;
		}

		status = maintenanceRecord != null && maintenanceRecord.isDraftMode() && super.getRequest().getPrincipal().hasRealm(maintenanceRecord.getTechnician());

		super.getResponse().setAuthorised(status && statusTask);
	}

	@Override
	public void load() {
		MaintenanceRecordTask object;
		Integer maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		object = new MaintenanceRecordTask();
		object.setMaintenanceRecord(maintenanceRecord);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final MaintenanceRecordTask mrt) {
		;
	}

	@Override
	public void validate(final MaintenanceRecordTask mrt) {

		Task task = super.getRequest().getData("task", Task.class);
		super.state(task != null, "task", "technician.maintenance-record-task.form.error.no-task-to-unlink");
	}

	@Override
	public void perform(final MaintenanceRecordTask mrt) {
		Task task = super.getRequest().getData("task", Task.class);
		MaintenanceRecord maintenanceRecord = mrt.getMaintenanceRecord();

		this.repository.delete(this.repository.findMaintenanceRecordTaskByMaintenanceRecordAndTask(maintenanceRecord, task));

	}

	@Override
	public void unbind(final MaintenanceRecordTask mrt) {
		Collection<Task> tasks;
		int maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;
		SelectChoices choices;
		Dataset dataset;

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		tasks = this.repository.findValidTasksToUnlink(maintenanceRecord);
		choices = SelectChoices.from(tasks, "description", mrt.getTask());

		dataset = super.unbindObject(mrt, "maintenanceRecord");
		dataset.put("maintenanceRecordId", mrt.getMaintenanceRecord().getId());
		dataset.put("task", choices.getSelected().getKey());
		dataset.put("tasks", choices);
		dataset.put("aircraftRegistrationNumber", mrt.getMaintenanceRecord().getAircraft().getRegistrationNumber());

		super.getResponse().addData(dataset);
	}
}