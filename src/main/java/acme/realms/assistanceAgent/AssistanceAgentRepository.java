
package acme.realms.assistanceAgent;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AssistanceAgentRepository extends AbstractRepository {

	@Query("select c from AssistanceAgent c where c.employeeCode = :employeeCode")
	AssistanceAgent findAgentByEmployeeCode(String employeeCode);

	AssistanceAgent findAssistanceAgentByEmployeeCode(String employeeCode);

}
