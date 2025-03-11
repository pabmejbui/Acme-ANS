
package acme.entities.trackingLogs;

import java.beans.Transient;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;

public class TrackingLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	//Attributes
	@Mandatory
	@Column(unique = true)
	@Automapped
	private String				id;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	@Automapped
	private Date				lastUpdateMoment;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				step;

	@Mandatory
	@ValidNumber(min = 0, max = 100)
	@Automapped
	private Double				resolutionPercentage;

	@Mandatory
	@Valid
	@Automapped
	private Resolution			indicator;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				resolution;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				draftMode;


	// Derived attributes
	@Transient
	private String getResolution() {
		return null;
	}
}
