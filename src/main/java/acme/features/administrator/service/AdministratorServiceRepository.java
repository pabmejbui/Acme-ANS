
package acme.features.administrator.service;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.services.Service;

@Repository
public interface AdministratorServiceRepository extends AbstractRepository {

	@Query("SELECT s FROM Service s")
	Collection<Service> findAllServices();

	@Query("SELECT s FROM Service s WHERE s.id = :id")
	Service findServiceById(int id);
}
