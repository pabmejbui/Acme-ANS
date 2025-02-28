/*
 * Airport.java
 *
 * Copyright (C) 2025 Andrés García.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.entities.airports;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Airport extends AbstractEntity {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(max = 50)
	@NotBlank
	@Automapped
	private String				name;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{3}$")
	@NotBlank
	@Column(unique = true)
	@Automapped
	private String				iataCode;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@NotNull
	@Automapped
	private OperationalScope	operationalScope;

	@Mandatory
	@ValidString(max = 50)
	@NotBlank
	@Automapped
	private String				city;

	@Mandatory
	@ValidString(max = 50)
	@NotBlank
	@Automapped
	private String				country;

	@Optional
	@ValidUrl
	@Automapped
	private String				website;

	@Optional
	@ValidEmail
	@Automapped
	private String				email;

	@Optional
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	@Automapped
	private String				phoneNumber;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
