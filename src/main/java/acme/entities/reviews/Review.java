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
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Review extends AbstractEntity {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Automapped
	@Mandatory
	@ValidString(max = 50)
	private String				name;

	@Automapped
	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				moment;

	@Automapped
	@Mandatory
	@ValidString(max = 50)
	private String				subject;

	@Automapped
	@Mandatory
	@ValidString(max = 255)
	private String				text;

	@Automapped
	@Optional
	@ValidNumber(min = 0, max = 10, fraction = 2)
	private Double				score;

	@Automapped
	@Optional
	@Valid
	private Boolean				recommended;

	@Automapped
	@Mandatory
	@Valid
	private Boolean				draftMode;
}
