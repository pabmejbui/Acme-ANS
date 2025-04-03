
package acme.features.authenticated.customer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.realms.customer.Customer;

@Repository
public interface AuthenticatedCustomerRepository extends AbstractRepository {

	@Query("select c from Customer c where c.userAccount.id = :accountId")
	Customer findOneCustomerrByUserAccountId(int accountId);

	@Query("select count(c) > 0 from Customer c where c.userAccount.id = :accountId")
	boolean isCustomer(int accountId);

	@Query("SELECT ua FROM UserAccount ua WHERE ua.id = :id")
	UserAccount findUserAccountById(int id);
}
