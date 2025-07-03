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
public interface TechnicianMaintenanceRecordTaskRepository extends AbstractRepository {

	@Query("select mr from MaintenanceRecord mr where mr.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("select t from Task t where t.id = :id")
	Task findTaskById(int id);

	@Query("select t from Task t where t not in (select mrt.task from MaintenanceRecordTask mrt where mrt.maintenanceRecord = :maintenanceRecord) and (t.draftMode = false or t.technician = :technician)")
	Collection<Task> findValidTasksToLink(MaintenanceRecord maintenanceRecord, Technician technician);

	@Query("select t from Task t where t in (select mrt.task from MaintenanceRecordTask mrt where mrt.maintenanceRecord = :maintenanceRecord)")
	Collection<Task> findValidTasksToUnlink(MaintenanceRecord maintenanceRecord);

	@Query("select mrt.task from MaintenanceRecordTask mrt where mrt.maintenanceRecord = :maintenanceRecord")
	Collection<MaintenanceRecordTask> findMaintenanceRecordTaskByMaintenanceRecord(MaintenanceRecord maintenanceRecord);

	@Query("select mrt from MaintenanceRecordTask mrt where mrt.maintenanceRecord = :maintenanceRecord and mrt.task = :task")
	MaintenanceRecordTask findMaintenanceRecordTaskByMaintenanceRecordAndTask(MaintenanceRecord maintenanceRecord, Task task);
}
