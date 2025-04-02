
package acme.entities.activityLog;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidActivityLog
public class ActivityLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Attributes
	@Mandatory
	@ValidMoment(past = true)
	@Automapped
	private Date				registrationMoment;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				incidentType;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@ValidNumber(min = 0, max = 10)
	@Automapped
	private Integer				severity;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Relationships

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private FlightAssignment	flightAssignment;
}
