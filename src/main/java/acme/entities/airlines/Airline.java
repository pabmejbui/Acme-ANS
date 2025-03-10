
package acme.entities.airlines;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidPhone;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Airline extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				name;

	@ValidString(pattern = "^[A-Z]{3}$") //Es normalmente una X, mirar como hacer eso.
	@Column(unique = true)
	@Automapped
	@Mandatory
	private String				iataCode;

	@Enumerated(EnumType.STRING)
	@Automapped
	@Mandatory
	private AirlineType			type;

	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	@Automapped
	@Mandatory
	private Date				foundationMoment;

	@ValidUrl
	@Automapped
	@Optional
	private String				website;

	@Automapped
	@Optional
	@ValidEmail
	private String				optionalEmail;

	@Optional
	@ValidPhone
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				draftMode;

	// Derived attributes

	// Relationships

}
