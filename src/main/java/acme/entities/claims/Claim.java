
package acme.entities.claims;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.entities.flights.Leg;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogStatus;
import acme.realms.assistanceAgent.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Claim extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@ValidEmail
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
	@Automapped
	private boolean				draftMode;

	//Derived attributes


	@Transient
	public ClaimIndicator getIndicator() {
		ClaimIndicator status;
		ClaimRepository repository;
		Collection<TrackingLog> tls;

		status = ClaimIndicator.PENDING;
		repository = SpringHelper.getBean(ClaimRepository.class);
		tls = repository.findTrackingLogsByClaimId(this.getId());
		for (TrackingLog tl : tls) {
			if (tl.getStatus() == TrackingLogStatus.ACCEPTED)
				status = ClaimIndicator.ACCEPTED;
			else if (tl.getStatus() == TrackingLogStatus.REJECTED)
				status = ClaimIndicator.REJECTED;

		}
		return status;

	}


	// Relationships
	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AssistanceAgent	assistanceAgent;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Leg				leg;
}
