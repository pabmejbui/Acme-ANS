package acme.features.technician.task;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceRecords.MaintenanceRecordTask;
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
		boolean status = false;
		Integer maintenanceRecordId = null;
		MaintenanceRecord maintenanceRecord;
		Technician technician;

		if (super.getRequest().hasData("maintenanceRecordId")) {
			maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", Integer.class);

			if (maintenanceRecordId != null) {
				maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

				if (maintenanceRecord != null) {
					technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
					status = maintenanceRecord.isDraftMode() && technician.equals(maintenanceRecord.getTechnician());
				}
			}
		} else
			status = true;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Task task;
		Technician technician;

		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

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
		;
	}

	@Override
	public void perform(final Task task) {
		Integer maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;
		MaintenanceRecordTask mrt;

		maintenanceRecordId = super.getRequest().hasData("maintenanceRecordId") ?
			super.getRequest().getData("maintenanceRecordId", Integer.class) : null;

		this.repository.save(task);

		if (maintenanceRecordId != null) {

			mrt = new MaintenanceRecordTask();
			maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

			mrt.setTask(task);
			mrt.setMaintenanceRecord(maintenanceRecord);

			this.repository.save(mrt);
		}
	}

	@Override
	public void unbind(final Task task) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(TaskType.class, task.getType());

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration", "draftMode");
		dataset.put("technician", task.getTechnician().getIdentity().getFullName());
		dataset.put("type", choices.getSelected().getKey());
		dataset.put("types", choices);
		if (super.getRequest().hasData("maintenanceRecordId"))
			dataset.put("maintenanceRecordId", super.getRequest().getData("maintenanceRecordId", Integer.class));

		super.getResponse().addData(dataset);
	}

}