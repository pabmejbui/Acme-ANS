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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidUuid;
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
	@Length(max = 50)
	@NotBlank
	@Automapped
	private String				name;

	@Mandatory
	@ValidUuid
	@Column(unique = true, length = 3)
	@Pattern(regexp = "^[A-Z]{3}$", message = "{validation.airport.iataCode}")
	private String				iataCode;

	@Mandatory
	@Automapped
	private OperationalScope	operationalScope;

	@Mandatory
	@Length(max = 50)
	@NotBlank
	@Automapped
	private String				city;

	@Mandatory
	@Length(max = 50)
	@NotBlank
	@Automapped
	private String				country;

	@URL
	@Automapped
	private String				website;

	@Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$", message = "{validation.airport.email}")
	@Automapped
	private String				email;

	@Pattern(regexp = "^\\+?\\d{6,15}$", message = "{validation.airport.phoneNumber}")
	@Automapped
	private String				phoneNumber;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
