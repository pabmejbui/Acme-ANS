
package acme.features.customer.recomendation;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService; // This import was missing from your provided skeleton
import acme.entities.recomendations.Recommendation; // Ensure this points to your Recommendation entity
import acme.realms.customer.Customer;

@GuiService
public class CustomerRecommendationShowService extends AbstractGuiService<Customer, Recommendation> {

	@Autowired
	protected CustomerRecommendationRepository repository;


	@Override
	public void authorise() {
		boolean isAuthorized;
		int poiEntryId;
		Recommendation targetPoi;

		poiEntryId = super.getRequest().getData("id", int.class);
		targetPoi = this.repository.findPoiEntryById(poiEntryId);

		Integer clientId = super.getRequest().getPrincipal().getActiveRealm().getId();

		isAuthorized = targetPoi != null && this.repository.verifiesArrivalAirportForClient(targetPoi.getAirport().getId(), clientId);

		super.getResponse().setAuthorised(isAuthorized);
	}

	@Override
	public void load() {
		Recommendation targetPoi;
		int entryIdentifier;

		entryIdentifier = super.getRequest().getData("id", int.class);
		targetPoi = this.repository.findPoiEntryById(entryIdentifier);

		super.getBuffer().addData(targetPoi);
	}

	@Override
	public void unbind(final Recommendation poiRecommendation) {
		Dataset dataset;

		dataset = super.unbindObject(poiRecommendation, "name", "category", "address", "city", "country", "website", "latitude", "longitude", "airport.name");

		if (poiRecommendation.getLatitude() != null && poiRecommendation.getLongitude() != null)
			dataset.put("hasLocation", true);

		super.getResponse().addData(dataset);
	}
}
