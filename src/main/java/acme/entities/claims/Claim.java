
package acme.entities.claims;

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
		ClaimIndicator result;
		ClaimRepository repository;
		TrackingLog trackingLog;

		repository = SpringHelper.getBean(ClaimRepository.class);
		trackingLog = repository.findLastTrackingLogByClaimId(this.getId()).stream().findFirst().orElse(null);
		if (trackingLog == null)
			result = null;
		else {
			TrackingLogStatus status = trackingLog.getStatus();
			if (status.equals(TrackingLogStatus.ACCEPTED))
				result = ClaimIndicator.ACCEPTED;
			else if (status.equals(TrackingLogStatus.REJECTED))
				result = ClaimIndicator.REJECTED;
			else
				result = ClaimIndicator.PENDING;
		}
		return result;

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
