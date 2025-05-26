package acme.features.technician.maintenanceRecord;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceRecords.MaintenanceStatus;
import acme.realms.technician.Technician;

@GuiService
public class TechnicianMaintenanceRecordCreateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	// AbstractGuiService interface -------------------------------------------
	@Override
	public void authorise() {

		String maintenanceRecordStatus;

		boolean correctStatus;
		String method;
		boolean authorised = false;

		method = super.getRequest().getMethod();

		if (method.equals("GET"))
			authorised = true;
		else {
			int id;
			int version;
			int aircraftId;
			Aircraft aircraft;

			id = super.getRequest().getData("id", int.class);
			version = super.getRequest().getData("version", int.class);

			maintenanceRecordStatus = super.getRequest().getData("status", String.class);
			correctStatus = "0".equals(maintenanceRecordStatus) || Arrays.stream(MaintenanceStatus.values()).map(MaintenanceStatus::name).anyMatch(name -> name.equals(maintenanceRecordStatus));

			aircraftId = super.getRequest().getData("aircraft", int.class);
			aircraft = this.repository.findAircraftById(aircraftId);
			boolean aircraftExists = this.repository.findAllAircrafts().contains(aircraft);
			authorised = (aircraftId == 0 || aircraftExists) && id == 0 && version == 0 && correctStatus;
		}

		super.getResponse().setAuthorised(authorised);
	}
	
	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		Date moment = MomentHelper.getCurrentMoment();
		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		maintenanceRecord = new MaintenanceRecord();
		maintenanceRecord.setDraftMode(true);
		maintenanceRecord.setTechnician(technician);
		maintenanceRecord.setMoment(moment);
		super.getBuffer().addData(maintenanceRecord);
	}
	
	@Override
	public void bind(final MaintenanceRecord maintenanceRecord) {
		super.bindObject(maintenanceRecord, "status", "nextInspectionDate", "estimatedCost", "notes", "aircraft");
	}

	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {

		if (!this.getBuffer().getErrors().hasErrors("nextInspectionDate"))
			super.state(maintenanceRecord.getNextInspectionDate().compareTo(maintenanceRecord.getMoment()) >= 0, "nextInspectionDate", "acme.validation.technician.maintenance-record.nextInspectionDate.message");
	}

	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		this.repository.save(maintenanceRecord);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		SelectChoices choices;
		Collection<Aircraft> aircrafts;
		SelectChoices aircraft;

		Dataset dataset;
		aircrafts = this.repository.findAllAircrafts();
		choices = SelectChoices.from(MaintenanceStatus.class, maintenanceRecord.getStatus());
		aircraft = SelectChoices.from(aircrafts, "id", maintenanceRecord.getAircraft());

		dataset = super.unbindObject(maintenanceRecord, "moment", "status", "nextInspectionDate", "estimatedCost", "notes", "aircraft", "draftMode");

		dataset.put("status", choices.getSelected().getKey());
		dataset.put("status", choices);
		dataset.put("aircraft", aircraft.getSelected().getKey());
		dataset.put("aircraft", aircraft);

		super.getResponse().addData(dataset);
	}

}
