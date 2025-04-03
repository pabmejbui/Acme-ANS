
package acme.entities.airlines;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import acme.constraints.airline.ValidAirline;
import acme.constraints.airline.ValidIataCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidAirline
public class Airline extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				name;

	@ValidIataCode
	@Column(unique = true)
	@Mandatory
	private String				iataCode;

	@ValidUrl
	@Automapped
	@Mandatory
	private String				website;

	@Automapped
	@Valid
	@Mandatory
	private AirlineType			type;

	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	@Mandatory
	private Date				foundationMoment;

	@Automapped
	@Optional
	@ValidEmail
	private String				optionalEmail;

	@Optional
	@ValidPhone
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Derived attributes

	// Relationships

}
