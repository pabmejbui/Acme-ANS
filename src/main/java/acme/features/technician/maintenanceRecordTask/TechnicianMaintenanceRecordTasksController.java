package acme.features.technician.maintenanceRecordTask;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.maintenanceRecords.MaintenanceRecordTask;
import acme.realms.Technician;

@GuiController
public class TechnicianMaintenanceRecordTasksController extends AbstractGuiController<Technician, MaintenanceRecordTask> {

	//Internal state --------------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordTasksCreateService	createService;

	@Autowired
	private TechnicianMaintenanceRecordTasksDeleteService	deleteService;

	//Constructors ----------------------------------------------------------------

	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
	}

}
