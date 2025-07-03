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
public class TechnicianMaintenanceRecordTaskCreateService extends AbstractGuiService<Technician, MaintenanceRecordTask> {

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
		Technician technician;
		Collection<Task> tasks;

		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		tasks = this.repository.findValidTasksToLink(maintenanceRecord, technician);

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

		super.bindObject(mrt, "task");

	}

	@Override
	public void validate(final MaintenanceRecordTask mrt) {
		;
	}

	@Override
	public void perform(final MaintenanceRecordTask mrt) {

		this.repository.save(mrt);

	}

	@Override
	public void unbind(final MaintenanceRecordTask mrt) {
		Technician technician;
		Collection<Task> tasks;
		int maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;
		SelectChoices choices;
		Dataset dataset;

		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		tasks = this.repository.findValidTasksToLink(maintenanceRecord, technician);
		choices = SelectChoices.from(tasks, "description", mrt.getTask());

		dataset = super.unbindObject(mrt, "maintenanceRecord");
		dataset.put("maintenanceRecordId", mrt.getMaintenanceRecord().getId());
		dataset.put("task", choices.getSelected().getKey());
		dataset.put("tasks", choices);
		dataset.put("aircraftRegistrationNumber", mrt.getMaintenanceRecord().getAircraft().getRegistrationNumber());

		super.getResponse().addData(dataset);

	}
}