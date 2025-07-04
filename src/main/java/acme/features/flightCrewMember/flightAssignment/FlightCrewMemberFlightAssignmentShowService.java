
package acme.features.flightCrewMember.flightAssignment;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.AssignmentStatus;
import acme.entities.flightAssignment.DutyType;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flights.Leg;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentShowService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int assignmentId;
		FlightCrewMember member;
		FlightAssignment assignment;

		assignmentId = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(assignmentId);
		member = assignment == null ? null : assignment.getFlightCrewMember();
		status = member != null && super.getRequest().getPrincipal().hasRealm(member);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment assignment;
		int assignmentId;

		assignmentId = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(assignmentId);

		super.getBuffer().addData(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;
		SelectChoices statuses;
		SelectChoices duties;
		SelectChoices selectedLegs;

		// 1. Prepara los datos básicos del objeto.
		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "draftMode");

		// 2. Maneja el FlightCrewMember como texto de solo lectura (esto estaba bien).
		if (assignment.getFlightCrewMember() != null)
			dataset.put("flightCrewMemberCode", assignment.getFlightCrewMember().getEmployeeCode());

		// 3. Proporciona las opciones para 'status' y 'duty', con el valor actual seleccionado.
		// El JSP necesita la lista completa para mostrar el texto correcto, aunque el campo esté deshabilitado.
		statuses = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
		duties = SelectChoices.from(DutyType.class, assignment.getDuty());

		dataset.put("statuses", statuses);
		dataset.put("duties", duties);

		// 4. Proporciona la opción para 'leg' de forma eficiente.
		// Creamos una lista que solo contiene el 'leg' asignado actualmente.
		Collection<Leg> assignedLegOnly = new ArrayList<>();
		if (assignment.getLeg() != null)
			assignedLegOnly.add(assignment.getLeg());
		selectedLegs = SelectChoices.from(assignedLegOnly, "flightNumber", assignment.getLeg());

		dataset.put("legs", selectedLegs);
		if (selectedLegs.getSelected() != null)
			dataset.put("leg", selectedLegs.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
