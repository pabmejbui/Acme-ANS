
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import acme.entities.aircrafts.Aircraft;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceRecords.MaintenanceStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianDashboard extends AbstractForm {
	// Serialisation identifier -----------------------------------------------

	private static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	Map<MaintenanceStatus, Integer>	numberOfRecordsGroupedByStatus;

	MaintenanceRecord				nearestInspectionMaintenanceRecord;

	private List<Aircraft>			higherTaskNumberAircrafts;

	Money							avgEstimatedCost;
	Money							minEstimatedCost;
	Money							maxEstimatedCost;
	Money							stdDevEstimatedCost;

	Double							avgTaskDuration;
	Double							minTaskDuration;
	Double							maxTaskDuration;
	Double							stdDevTaskDuration;

}
