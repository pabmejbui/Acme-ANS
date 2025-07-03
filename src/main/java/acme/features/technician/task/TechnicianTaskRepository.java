package acme.features.technician.task;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceRecords.MaintenanceRecordTask;
import acme.entities.tasks.Task;

@Repository
public interface TechnicianTaskRepository extends AbstractRepository {

	@Query("select t from Task t where t.id = :id")
	Task findTaskById(int id);

	@Query("select mr from MaintenanceRecord mr where mr.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("select mrt.task from MaintenanceRecordTask mrt where mrt.maintenanceRecord.id = :id")
	Collection<Task> findTasksByMasterId(int id);

	@Query("select mrt.maintenanceRecord from MaintenanceRecordTask mrt where mrt.task.id = :id")
	Collection<MaintenanceRecord> findMaintenanceRecordsByTaskId(int id);

	@Query("select t from Task t where t.technician.id = :id")
	Collection<Task> findTasksByTechnicianId(int id);

	@Query("select t from Task t where t.draftMode = false")
	Collection<Task> findPublishedTasks();

	@Query("select mrt from MaintenanceRecordTask mrt where mrt.task.id = :id")
	Collection<MaintenanceRecordTask> findMaintenanceRecordTaskByTaskId(int id);

}
