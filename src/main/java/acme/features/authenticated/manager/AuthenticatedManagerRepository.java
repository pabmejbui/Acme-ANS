
package acme.features.authenticated.manager;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.realms.Manager;

@Repository
public interface AuthenticatedManagerRepository extends AbstractRepository {

	@Query("select m from Manager m where m.userAccount.id = :accountId")
	Manager findOneManagerByUserAccountId(int accountId);

	@Query("select count(m) > 0 from Manager m where m.userAccount.id = :accountId")
	boolean isManager(int accountId);

	@Query("SELECT ua FROM UserAccount ua WHERE ua.id = :id")
	UserAccount findUserAccountById(int id);
}
