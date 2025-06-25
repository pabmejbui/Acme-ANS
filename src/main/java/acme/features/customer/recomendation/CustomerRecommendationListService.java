
package acme.features.customer.recomendation;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.entities.recomendations.Recommendation;
import acme.realms.customer.Customer;

@Service
public class CustomerRecommendationListService extends AbstractGuiService<Customer, Recommendation> {

	@Autowired
	protected CustomerRecommendationRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int clientId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<Recommendation> clientArrivalPois = this.repository.retrievePoisForCustomerArrivals(clientId);
		super.getBuffer().addData(clientArrivalPois);
	}

	@Override
	public void unbind(final Recommendation recommendationEntry) {
		Dataset dataset = super.unbindObject(recommendationEntry, "name", "category", "city", "website");
		super.getResponse().addData(dataset);
	}
}
