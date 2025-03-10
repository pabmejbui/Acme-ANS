
package acme.entities.flightAssignment;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;

import acme.entities.flightCrewMembers.FlightCrewMember;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightAssignment extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Attributes
	@Mandatory
	@Valid
	@Automapped
	private DutyType			duty;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				moment;

	@Mandatory
	@Valid
	@Automapped
	private AssignmentStatus	status;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				remarks;

	// Relationships

	@Mandatory
	@ManyToOne(optional = false)
	@Automapped
	private FlightCrewMember	flightCrewMember;

}
