package acme.features.technician.maintenanceRecordTask;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.maintenanceRecords.MaintenanceRecordTask;
import acme.realms.technician.Technician;

@GuiController
public class TechnicianMaintenanceRecordTaskController extends AbstractGuiController<Technician, MaintenanceRecordTask> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordTaskCreateService	createService;

	@Autowired
	private TechnicianMaintenanceRecordTaskDeleteService	deleteService;

	// Constructors -----------------------------------------------------------

	@PostConstruct
	protected void initialise() {

		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
	}

}