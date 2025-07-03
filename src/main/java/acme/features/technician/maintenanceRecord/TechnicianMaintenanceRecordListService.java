package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.realms.technician.Technician;

@GuiService
public class TechnicianMaintenanceRecordListService extends AbstractGuiService<Technician, MaintenanceRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<MaintenanceRecord> maintenanceRecords;
		int technicianId;
		boolean mine;
		boolean showCreate = false;

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		mine = super.getRequest().hasData("mine", boolean.class);

		if (mine) {
			maintenanceRecords = this.repository.findMaintenanceRecordsByTechnicianId(technicianId);
			showCreate = true;
		} else
			maintenanceRecords = this.repository.findPublishedMaintenanceRecords();

		super.getResponse().addGlobal("showCreate", showCreate);
		super.getBuffer().addData(maintenanceRecords);

	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Dataset dataset;

		dataset = super.unbindObject(maintenanceRecord, "moment", "status", "nextInspectionDate", "estimatedCost", "notes", "draftMode");
		super.addPayload(dataset, maintenanceRecord,
			"estimatedCost", "notes", "draftMode", "aircraft.model",
			"aircraft.registrationNumber", "technician.identity.fullName",
			"technician.licenseNumber", "technician.phoneNumber");

		super.getResponse().addData(dataset);
	}
}
