package acme.features.technician.task;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecords.MaintenanceRecordTask;
import acme.entities.tasks.Task;
import acme.realms.Technician;

@Repository
public interface TechnicianTaskRepository extends AbstractRepository {
	
	@Query("SELECT t FROM Task t")
	Collection<Task> findAllTasks();
	
	@Query("SELECT t FROM Task t WHERE t.id = :id")
	Task findTaskById(int id);
	
	@Query("SELECT t FROM Task t WHERE t.technician.id = :technicianId")
	Collection<Task> findTasksByTechnicianId(int technicianId);
	
	@Query("SELECT mt FROM MaintenanceRecordTask mt WHERE mt.task.id = :taskId")
	Collection<MaintenanceRecordTask> findMaintenanceRecordTasksByTaskId(int taskId);

	@Query("SELECT t FROM Technician t WHERE t.userAccount.id = :userAccountId")
	Technician findTechnicianByUserAccountId(int userAccountId);
}
