/*
 * Review.java
 *
 * Copyright (C) 2025 Andrés García.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.entities.reviews;

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
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.airlines.Airline;
import acme.entities.airports.Airport;
import acme.entities.flights.Flight;
import acme.entities.services.Service;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Review extends AbstractEntity {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				name;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				moment;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				subject;

	@Mandatory
	@ValidString(min = 1, max = 255)
	@Automapped
	private String				text;

	@Optional
	@ValidNumber(min = 0, max = 10, fraction = 2)
	@Automapped
	private Double				score;

	@Mandatory
	@Automapped
	private boolean				recommended;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Relationships  ---------------------------------------------------------

	@Optional
	@Valid
	@ManyToOne
	private Flight				flight;

	@Optional
	@Valid
	@ManyToOne
	private Airport				airport;

	@Optional
	@Valid
	@ManyToOne
	private Service				service;

	@Optional
	@Valid
	@ManyToOne
	private Airline				airline;


	public boolean isExactlyOneRelationDefined() {
		int count = 0;
		if (this.flight != null)
			count++;
		if (this.airport != null)
			count++;
		if (this.service != null)
			count++;
		if (this.airline != null)
			count++;
		return count == 1;
	}

}
