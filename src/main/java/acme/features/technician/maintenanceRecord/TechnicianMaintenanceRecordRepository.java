package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircrafts.Aircraft;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceRecords.MaintenanceRecordTask;
import acme.entities.tasks.Task;

@Repository
public interface TechnicianMaintenanceRecordRepository extends AbstractRepository {

	@Query("select mr from MaintenanceRecord mr where mr.technician.id = :id")
	Collection<MaintenanceRecord> findMaintenanceRecordsByTechnicianId(int id);

	@Query("select mr from MaintenanceRecord mr where mr.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("select mr from MaintenanceRecord mr where mr.draftMode = false")
	Collection<MaintenanceRecord> findPublishedMaintenanceRecords();

	@Query("select a from Aircraft a")
	Collection<Aircraft> findAllAircrafts();

	@Query("select mrt.task from MaintenanceRecordTask mrt where mrt.maintenanceRecord.id = :id")
	Collection<Task> findTasksByMaintenanceRecordId(int id);

	@Query("select mrt from MaintenanceRecordTask mrt where mrt.maintenanceRecord.id = :id")
	Collection<MaintenanceRecordTask> findMaintenanceRecordTaskByMaintenanceRecordId(int id);

	@Query("select a from Aircraft a where a.id = :id")
	Aircraft findAircraftById(int id);
}
