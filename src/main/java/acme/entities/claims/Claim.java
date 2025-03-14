
package acme.entities.claims;

import java.time.Duration;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.entities.trackingLogs.Resolution;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Claim extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	//Attributes
	@Mandatory
	@Column(unique = true)
	@Automapped
	private String				identifier;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@ValidString(pattern = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
	@Automapped
	private String				passengerEmail;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@Automapped
	private ClaimType			type;

	@Mandatory
	@Valid
	@Automapped
	private Resolution			indicator;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	//Derived attributes
	@Transient
	private Duration			getTimeSinceRegistration;

	@Transient
	private Integer				getNumberOfTrackingLogs;

	// Relationships
}
