
package acme.entities.flights;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.realms.Manager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	// Serialisation version

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Column(unique = true)
	@Automapped
	private String				tag;

	@Mandatory
	@Valid
	@Automapped
	private Indication			indication;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				cost;

	@Optional
	@ValidString
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				draftMode;

	// Derived attributes

	@Transient
	private Date				scheduledDeparture;

	@Transient
	private Date				scheduledArrival;

	@Transient
	private String				originCity;

	@Transient
	private String				destinationCity;

	@Transient
	private Integer				numberOfLayovers;

	// Relationships

	@Mandatory
	@ManyToOne(optional = false)
	@Automapped
	private Manager				manager;

}
