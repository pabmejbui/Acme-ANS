
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.AssignmentStatus;
import acme.entities.flightAssignment.DutyType;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flights.Leg;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentUpdateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status = false;
		int id;
		FlightAssignment assignment;
		int currentUserId;

		// Obtiene el ID del assignment recibido desde el request
		id = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(id);

		// ID del usuario actualmente autenticado
		currentUserId = super.getRequest().getPrincipal().getActiveRealm().getId();

		// Solo autoriza si el assignment está en borrador y pertenece al usuario actual
		if (assignment != null)
			status = assignment.isDraftMode() && assignment.getFlightCrewMember().getId() == currentUserId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment assignment;
		int id;
		id = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(id);
		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		//Que salte error 500 si el value de la leg no existe
		if (legId != 0) {
			leg = this.repository.findLegById(legId);

			// 1️) Si el leg no existe → error 500
			if (leg == null)
				throw new IllegalStateException("El leg indicado no existe");

			// 2️) Si el leg no pertenece a la aerolínea del miembro loggeado → error 500
			FlightCrewMember activeMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
			if (!leg.getAircraft().getAirline().equals(activeMember.getAirline()))
				throw new IllegalStateException("No tienes permiso para modificar este leg");
		}

		super.bindObject(assignment, "duty", "status", "remarks");
		assignment.setLastUpdate(MomentHelper.getCurrentMoment());
		assignment.setLeg(leg);
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		Leg leg = assignment.getLeg();

		if (leg == null)
			super.state(false, "leg", "flightAssignment.error.leg-required");
		else {
			boolean isDraft = leg.isDraftMode();  // true si está en borrador (no publicado)
			super.state(!isDraft, "leg", "flightAssignment.error.leg-not-published");
		}
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;
		SelectChoices statuses;
		SelectChoices duties;
		Collection<Leg> legs;
		SelectChoices selectedLegs;
		String employeeCode;
		FlightCrewMember member;
		Collection<FlightCrewMember> members;
		SelectChoices selectedMembers;

		member = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		// Tripulantes de la aerolínea del miembro activo
		members = this.repository.findCrewMembersByAirline(member.getAirline());
		SelectChoices selectedCrew = SelectChoices.from(members, "employeeCode", assignment.getFlightCrewMember());

		// legs de vuelo
		legs = this.repository.findPublishedFutureOwnedLegs(MomentHelper.getCurrentMoment(), member.getAirline());
		Leg currentLeg = assignment.getLeg();
		if (currentLeg != null && !legs.contains(currentLeg))
			legs.add(currentLeg);

		// Opciones de estado y duty
		statuses = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
		duties = SelectChoices.from(DutyType.class, assignment.getDuty());
		selectedLegs = SelectChoices.from(legs, "flightNumber", assignment.getLeg());

		// Código de empleado del tripulante asignado
		employeeCode = assignment.getFlightCrewMember() != null ? assignment.getFlightCrewMember().getEmployeeCode() : null;
		selectedMembers = SelectChoices.from(members, "employeeCode", assignment.getFlightCrewMember());

		// Dataset principal
		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "draftMode");
		dataset.put("employeeCode", employeeCode);
		dataset.put("statuses", statuses);
		dataset.put("duties", duties);

		if (selectedLegs.getSelected() != null)
			dataset.put("leg", selectedLegs.getSelected().getKey());
		dataset.put("legs", selectedLegs);

		if (selectedCrew.getSelected() != null)
			dataset.put("flightCrewMember", selectedCrew.getSelected().getKey());
		dataset.put("crewMembers", selectedMembers);

		// 🔹 Campo de solo lectura con el código del tripulante
		if (assignment.getFlightCrewMember() != null)
			dataset.put("flightCrewMemberCode", assignment.getFlightCrewMember().getEmployeeCode());

		super.getResponse().addData(dataset);
	}

}
