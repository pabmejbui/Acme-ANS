
package acme.entities.passenger;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Passenger extends AbstractEntity {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(max = 256)
	@Automapped
	private String				fullName;

	@Mandatory
	@ValidEmail
	@Automapped
	private String				email;

	@Mandatory
	@ValidString(pattern = "^[A-Z0-9]{6,9}$", message = "{acme.validation.passportNumber.notPattern.message}")
	@Automapped
	private String				passportNumber;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				dateOfBirth;

	@Optional
	@ValidString(max = 51)
	@Automapped
	private String				specialNeeds;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Derived attributes--------------------------------

	// Relationships-------------------------------------
}
