package acme.features.technician.maintenanceRecordTask;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceRecords.MaintenanceRecordTask;
import acme.entities.tasks.Task;
import acme.realms.technician.Technician;

@Repository
public interface TechnicianMaintenanceRecordTasksRepository extends AbstractRepository {
	
	@Query("SELECT t FROM Task t WHERE t.id = :taskId")
	Task findTaskById(int taskId);
	
	@Query("SELECT m FROM MaintenanceRecord m WHERE m.id = :maintenanceRecordId")
	MaintenanceRecord findMaintenanceRecordById(int maintenanceRecordId);
	
	@Query("SELECT t FROM Task t WHERE t.technician.id = :technicianI")
	Collection<Task> findTasksByTechnicianId(int technicianI);
	
	@Query("SELECT mt.task FROM MaintenanceRecordTask mt WHERE mt.maintenanceRecord.id = :maintenanceRecordId")
	Collection<Task> findTasksOfMaintenanceRecord(int maintenanceRecordId);
	
	@Query("SELECT mt FROM MaintenanceRecordTask mt WHERE mt.maintenanceRecord.id = :maintenanceRecordId and mt.task.id = :taskId")
	MaintenanceRecordTask findMaintenanceRecordTaskByMaintenanceRecordIdAndTaskId(int maintenanceRecordId, int taskId);
	
	@Query("SELECT t FROM Technician t WHERE t.userAccount.id = :userAccountId")
	Technician findTechnicianByUserAccountId(int userAccountId);
	
	@Query("SELECT COUNT(mt) FROM MaintenanceRecordTask mt WHERE mt.maintenanceRecord.id = :maintenanceRecordId AND mt.task.id = :taskId")
	int countMaintenanceRecordTasksByMaintenanceRecordIdAndTaskId(int maintenanceRecordId, int taskId);

}
