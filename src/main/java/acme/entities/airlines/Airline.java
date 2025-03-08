
package acme.entities.airlines;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Airline extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@NotBlank
	@ValidString(max = 50)
	@Automapped
	private String				name;

	@NotBlank
	@ValidString(pattern = "^[A-Z]{2}X$")
	@Column(unique = true)
	@Automapped
	@Mandatory
	private String				iataCode;

	@NotBlank
	@ValidUrl
	@Automapped
	@Mandatory
	private String				website;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Automapped
	@Mandatory
	private AirlineType			type;

	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	@Automapped
	@Mandatory
	private Date				foundationMoment;

	@Automapped
	@Optional
	@ValidEmail
	private String				email;

	@Optional
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				draftMode;

	// Derived attributes

	// Relationships

}
