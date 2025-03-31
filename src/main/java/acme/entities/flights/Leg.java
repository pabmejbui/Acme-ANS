
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
import acme.entities.airlines.Airline;
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
	private Date				scheduledDeparture;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment
	private Date				scheduledArrival;

	@Mandatory
	@Valid
	@Automapped
	private LegStatus			status;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Derived attributes


	@Transient
	public Double getDuration() {
		Double durationHours;
		durationHours = (this.getScheduledArrival().getTime() - this.getScheduledDeparture().getTime()) / (1000.0 * 60 * 60);
		return durationHours;
	}

	// Relationships


	@Mandatory
	@ManyToOne(optional = false)
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
	private Aircraft	aircraft;

	@Mandatory
	@ManyToOne(optional = false)
	private Airline		airline;
}
