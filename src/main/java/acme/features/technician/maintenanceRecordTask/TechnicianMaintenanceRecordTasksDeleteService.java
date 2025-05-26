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
public class TechnicianMaintenanceRecordTasksDeleteService extends AbstractGuiService<Technician, MaintenanceRecordTask> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordTasksRepository repository;


	// AbstractGuiService interface -------------------------------------------
	
	@Override
	public void authorise() {
		int technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		int maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		MaintenanceRecord maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		boolean status = maintenanceRecord != null && maintenanceRecord.getDraftMode() && maintenanceRecord.getTechnician().getId() == technicianId;

		if (status) {
			String method;

			method = super.getRequest().getMethod();
			if (method.equals("GET"))
				status = true;
			else {
				int taskId = super.getRequest().getData("task", int.class);
				MaintenanceRecordTask maintenanceRecordTask = this.repository.findMaintenanceRecordTaskByMaintenanceRecordIdAndTaskId(maintenanceRecordId, taskId);
				status = taskId == 0 || maintenanceRecordTask != null;

			}
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		MaintenanceRecord maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		MaintenanceRecordTask maintenanceRecordTask = new MaintenanceRecordTask();
		maintenanceRecordTask.setMaintenanceRecord(maintenanceRecord);

		super.getBuffer().addData(maintenanceRecordTask);
	}

	@Override
	public void bind(final MaintenanceRecordTask maintenanceRecordTask) {
		super.bindObject(maintenanceRecordTask, "task");
	}

	@Override
	public void validate(final MaintenanceRecordTask maintenanceRecordTask) {
		Task task;
		task = super.getRequest().getData("task", Task.class);
		if (!this.getBuffer().getErrors().hasErrors("task"))
			super.state(task != null, "task", "acme.validation.technician.involves.noTask.message");
	}

	@Override
	public void perform(final MaintenanceRecordTask maintenanceRecordTask) {
		int taskId = super.getRequest().getData("task", Task.class).getId();
		int maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);

		this.repository.delete(this.repository.findMaintenanceRecordTaskByMaintenanceRecordIdAndTaskId(maintenanceRecordId, taskId));
	}

	@Override
	public void unbind(final MaintenanceRecordTask maintenanceRecordTask) {
		SelectChoices task;
		Dataset dataset;

		int maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);

		Collection<Task> tasks = this.repository.findTasksOfMaintenanceRecord(maintenanceRecordId);

		task = SelectChoices.from(tasks, "id", maintenanceRecordTask.getTask());

		dataset = super.unbindObject(maintenanceRecordTask, "maintenanceRecord", "task");

		dataset.put("task", task);
		dataset.put("maintenanceRecordId", maintenanceRecordId);

		super.getResponse().addData(dataset);
	}
}
