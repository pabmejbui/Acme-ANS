/*
 * Task.java
 *
 * Copyright (C) 2025 Andrés García.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.entities.tasks;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Task extends AbstractEntity {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	@Automapped
	@Mandatory
	@Enumerated(EnumType.STRING)
	private Type				type;

	@Automapped
	@Mandatory
	@ValidString(max = 255)
	private String				description;

	@Automapped
	@Mandatory
	@ValidNumber(min = 0, max = 10)
	private Integer				priority;

	@Automapped
	@Mandatory
	@ValidNumber(min = 0)
	private Integer				estimatedDuration;

	@Automapped
	@Mandatory
	@Valid
	private Boolean				draftMode;
}
