
package acme.features.assistanceAgent.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.trackingLogs.Resolution;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogShowService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal State --------------------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractGuiService ----------------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int currentAssistanceAgentId;
		int trackingLogId;
		TrackingLog selectedTrackingLog;
		Principal principal;

		// Obtener el principal (usuario autenticado)
		principal = super.getRequest().getPrincipal();

		// Obtener el ID del agente de asistencia actual y el ID del tracking log
		currentAssistanceAgentId = principal.getActiveRealm().getId();
		trackingLogId = super.getRequest().getData("id", int.class);
		selectedTrackingLog = this.repository.findTrackingLogById(trackingLogId);

		// Autorizar solo si el agente de asistencia tiene permisos sobre el tracking log
		status = principal.hasRealmOfType(AssistanceAgent.class) && selectedTrackingLog.getClaim().getAssistanceAgent().getId() == currentAssistanceAgentId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog trackingLog;
		int trackingLogId;

		// Obtener el ID del tracking log y cargarlo
		trackingLogId = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(trackingLogId);

		// Agregar el tracking log al buffer para la vista
		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		SelectChoices indicators;

		// Obtener los tipos de indicadores disponibles
		indicators = SelectChoices.from(Resolution.class, trackingLog.getIndicator());

		// Crear el dataset con la información del tracking log
		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "step", "resolutionPercentage", "indicator", "resolution", "draftMode");

		// Agregar los SelectChoices de indicadores al dataset
		dataset.put("indicators", indicators);

		// Añadir el dataset a la respuesta
		super.getResponse().addData(dataset);
	}
}
