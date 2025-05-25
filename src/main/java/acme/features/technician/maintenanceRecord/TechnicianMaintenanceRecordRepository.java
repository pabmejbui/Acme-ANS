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
	
	@Query("SELECT m FROM MaintenanceRecord m")
	Collection<MaintenanceRecord> findAllMaintenanceRecords();
	
	@Query("SELECT m FROM MaintenanceRecord m WHERE m.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);
	
	@Query("SELECT m FROM MaintenanceRecord m WHERE m.technician.id = :technicianId")
	Collection<MaintenanceRecord> findMaintenanceRecordsByTechnicianId(int technicianId);

	@Query("SELECT a FROM Aircraft a")
	Collection<Aircraft> findAllAircrafts();
	
	@Query("SELECT mt.task from MaintenanceRecordTask mt WHERE mt.maintenanceRecord.id = :id")
	Collection<Task> findTasksByMaintenanceRecordId(int id);
	
	@Query("SELECT mt FROM MaintenanceRecordTask mt WHERE mt.maintenanceRecord.id = :id")
	Collection<MaintenanceRecordTask> findMaintenanceRecordTasksByMaintenanceRecordId(int id);
	
	@Query("SELECT COUNT(mt.task) FROM MaintenanceRecordTask mt WHERE mt.maintenanceRecord.id = :id")
	int countTasksByMaintenanceRecordId(int id);
	
	@Query("SELECT COUNT(mt.task) FROM MaintenanceRecordTask mt WHERE mt.maintenanceRecord.id = :id AND mt.task.draftMode = true")
	int countNotPublishedTasksByMaintenanceRecordId(int id);
}
