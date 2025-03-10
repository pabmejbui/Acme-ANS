
package acme.entities.flights;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.constraints.flights.ValidFlightNumber;
import acme.constraints.flights.ValidLeg;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidFlightNumber
@ValidLeg
public class Leg extends AbstractEntity {

	// Serialisation version

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@Column(unique = true)
	@Automapped
	private String				flightNumber;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment
	@Automapped
	private Date				scheduledDeparture;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment
	@Automapped
	private Date				scheduledArrival;

	@Mandatory
	@Valid
	@Automapped
	private LegStatus			status;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				draftMode;

	// Derived attributes


	@Transient
	public Double getDuration() {
		if (this.scheduledDeparture == null || this.scheduledArrival == null)
			return null;

		long milliseconds = this.scheduledArrival.getTime() - this.scheduledDeparture.getTime();

		return this.millisecondsToHours(milliseconds);
	}

	private double millisecondsToHours(final long milliseconds) {
		return milliseconds / (1000.0 * 60 * 60);
	}

	// Relationships


	@Mandatory
	@ManyToOne(optional = false)
	@Automapped
	private Flight		flight;

	@Mandatory
	@ManyToOne(optional = false)
	@Automapped
	private Airport		originAirport;

	@Mandatory
	@ManyToOne(optional = false)
	@Automapped
	private Airport		destinationAirport;

	@Mandatory
	@ManyToOne(optional = false)
	@Automapped
	private Aircraft	aircraft;
}
