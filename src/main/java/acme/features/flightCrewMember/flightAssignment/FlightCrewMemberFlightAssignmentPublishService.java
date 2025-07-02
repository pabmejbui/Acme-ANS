
package acme.features.flightCrewMember.flightAssignment;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.entities.flightAssignment.AssignmentStatus;
import acme.entities.flightAssignment.DutyType;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flights.Leg;
import acme.realms.flightCrewMembers.FlightCrewMember;

@Service
public class FlightCrewMemberFlightAssignmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		FlightAssignment assignment;
		boolean validRequestMethod = true;
		boolean validStatus = true;
		boolean validDuty = true;
		boolean validLeg = true;
		boolean correctMember = false;
		int assignmentId;

		if (super.getRequest().getMethod().equals("GET"))
			validRequestMethod = false;
		else {
			// Validar que el duty existe en el enum
			String dutyString = super.getRequest().getData("duty", String.class);
			validDuty = Arrays.stream(DutyType.values()).anyMatch(d -> d.toString().equals(dutyString));
			validDuty = validDuty || dutyString.equals("0");  // Para cuando aún no se ha seleccionado nada

			// Validar que el status existe en el enum
			String statusString = super.getRequest().getData("status", String.class);
			validStatus = Arrays.stream(AssignmentStatus.values()).anyMatch(s -> s.toString().equals(statusString));
			validStatus = validStatus || statusString.equals("0");

			// Validar que el Leg es válido
			int legId = super.getRequest().getData("leg", int.class);
			Collection<Leg> validLegs = this.repository.findPublishedFutureLegs(MomentHelper.getCurrentMoment());
			validLeg = legId != 0 && validLegs.stream().anyMatch(l -> l.getId() == legId);

			// Validar que el assignment pertenece al usuario y está en draft
			assignmentId = super.getRequest().getData("id", int.class);
			assignment = this.repository.findFlightAssignmentById(assignmentId);

			if (assignment != null) {
				int principalId = super.getRequest().getPrincipal().getActiveRealm().getId();
				correctMember = assignment.getFlightCrewMember().getId() == principalId && assignment.isDraftMode();
			}
		}

		boolean authorised = validRequestMethod && validDuty && validStatus && validLeg && correctMember;
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.repository.findFlightAssignmentById(id);
		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		// El FlightCrewMember ya no se modifica, viene cargado en el objeto 'assignment'.
		int legId = super.getRequest().getData("leg", int.class);
		Leg leg = this.repository.findLegById(legId);

		super.bindObject(assignment, "duty", "status", "remarks");
		assignment.setLeg(leg);
		assignment.setLastUpdate(MomentHelper.getCurrentMoment());
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		// La validación del Leg debe hacerse solo si se ha proporcionado uno.
		if (assignment.getLeg() != null) {
			boolean notYetOccurred = MomentHelper.isAfter(assignment.getLeg().getScheduledArrival(), MomentHelper.getCurrentMoment());
			super.state(notYetOccurred, "leg", "flight-crew-member.flight-assignment.leg-has-not-finished-yet");

			boolean isDraft = assignment.getLeg().isDraftMode();
			super.state(!isDraft, "leg", "flightAssignment.error.leg-not-published");
		} else
			// Si el leg es nulo, es un error de validación.
			super.state(false, "leg", "flightAssignment.error.leg-required");
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		assignment.setDraftMode(false);
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;
		SelectChoices statuses;
		SelectChoices duties;
		Collection<Leg> legs;
		SelectChoices selectedLegs;
		// La colección de todos los miembros ya no es necesaria.

		// Solo se muestran legs futuros y publicados.
		legs = this.repository.findPublishedFutureLegs(MomentHelper.getCurrentMoment());

		statuses = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
		duties = SelectChoices.from(DutyType.class, assignment.getDuty());
		selectedLegs = SelectChoices.from(legs, "flightNumber", assignment.getLeg());

		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "draftMode");

		// Se añade el código del empleado para mostrarlo en el campo de solo lectura.
		if (assignment.getFlightCrewMember() != null)
			dataset.put("flightCrewMemberCode", assignment.getFlightCrewMember().getEmployeeCode());

		dataset.put("statuses", statuses);
		dataset.put("duties", duties);

		// Se gestiona el caso en que no haya un leg seleccionado.
		dataset.put("leg", selectedLegs.getSelected() != null ? selectedLegs.getSelected().getKey() : "");
		dataset.put("legs", selectedLegs);

		// Los campos 'member' y 'members' ya no se añaden al dataset.

		super.getResponse().addData(dataset);
	}
}
