
package acme.features.flightStatus;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.airports.Airport;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightStatus extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	//Attributes -------------------------------------------------------
	@Mandatory
	@ValidString
	@Automapped
	private String				flightNumber;

	@Mandatory
	@ValidString
	@Automapped
	private String				airline;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
	private Date				scheduledTime;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment
	private Date				actualTime;

	@Mandatory
	@ValidString
	@Automapped
	private String				status;

	@ValidNumber(min = 0)
	@Automapped
	private Integer				delayMinutes;

	//Relationship ---------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport				airport;

}
