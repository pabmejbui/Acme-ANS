
package acme.features.assistanceAgent.tracking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogList extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal State --------------------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractGuiService ----------------------------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int claimId;
		Collection<TrackingLog> trackingLogs;

		claimId = super.getRequest().getData("claimId", int.class);

		trackingLogs = this.repository.findTrackingLogsByClaimId(claimId);
		super.getBuffer().addData(trackingLogs);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "step", "resolutionPercentage", "indicator", "resolution");

		super.getResponse().addData(dataset);

	}

	@Override
	public void unbind(final Collection<TrackingLog> objects) {
		int claimId;

		claimId = super.getRequest().getData("claimId", int.class);

		super.getResponse().addGlobal("claimId", claimId);
	}
}
