package acme.features.technician.maintenanceRecordTask;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceRecords.MaintenanceRecordTask;
import acme.entities.tasks.Task;
import acme.realms.technician.Technician;

@GuiService
public class TechnicianMaintenanceRecordTasksCreateService extends AbstractGuiService<Technician, MaintenanceRecordTask> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordTasksRepository repository;

	// AbstractGuiService interface -------------------------------------------
	
	@Override
	public void authorise() {

		boolean authorised;
		Principal principal = super.getRequest().getPrincipal();
		int userAccountId = principal.getAccountId();

		int maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		MaintenanceRecord maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);
		Technician technician = this.repository.findTechnicianByUserAccountId(userAccountId);
		
		if (maintenanceRecord != null) {
			authorised = technician.getId() == maintenanceRecord.getTechnician().getId() && maintenanceRecord.getDraftMode() == true;
			if (authorised) {
				String method = super.getRequest().getMethod();

				if (method.equals("GET"))
					authorised = true;
				else {
					int id;
					int version;
					int taskId;
					int sameTask;
					Task task;

					id = super.getRequest().getData("id", int.class);
					version = super.getRequest().getData("version", int.class);

					taskId = super.getRequest().getData("task", int.class);
					sameTask = this.repository.countMaintenanceRecordTasksByMaintenanceRecordIdAndTaskId(maintenanceRecordId, taskId);
					task = this.repository.findTaskById(taskId);
					boolean taskExists = this.repository.findTasksByTechnicianId(technician.getId()).contains(task);
					authorised = (taskId == 0 || taskExists) && id == 0 && version == 0 && technician != null && sameTask < 1;
				}
			}
			super.getResponse().setAuthorised(authorised);
		}
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

	}

	@Override
	public void perform(final MaintenanceRecordTask maintenanceRecordTask) {
		this.repository.save(maintenanceRecordTask);
	}

	@Override
	public void unbind(final MaintenanceRecordTask maintenanceRecordTask) {
		SelectChoices task;
		Dataset dataset;

		int technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		int maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);

		Collection<Task> taskOnMaintenanceRecord = this.repository.findTasksOfMaintenanceRecord(maintenanceRecordId);

		Collection<Task> tasks = this.repository.findTasksByTechnicianId(technicianId).stream().filter(t -> !taskOnMaintenanceRecord.contains(t)).toList();

		task = SelectChoices.from(tasks, "id", maintenanceRecordTask.getTask());

		dataset = super.unbindObject(maintenanceRecordTask, "maintenanceRecord", "task");

		dataset.put("task", task);
		dataset.put("maintenanceRecordId", maintenanceRecordId);

		super.getResponse().addData(dataset);
	}

}
