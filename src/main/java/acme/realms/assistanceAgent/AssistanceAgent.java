
package acme.realms.assistanceAgent;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.entities.airlines.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class AssistanceAgent extends AbstractRole {

	// Serialisation version -----------------------------------------------------------------------------------------
	private static final long	serialVersionUID	= 1L;
	// Attributes ----------------------------------------------------------------------------------------------------

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	@Column(unique = true)

	private String				employeeCode;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 255)
	private String				languages;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				beginDate;

	@Optional
	@Automapped
	@ValidMoney
	private Money				salary;

	@Optional
	@Automapped
	@ValidString(min = 1, max = 255)
	private String				bio;

	@Optional
	@Automapped
	@ValidUrl
	private String				photoLink;

	// Relationships -------------------------------------------------------------------------------------------------

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Airline				airline;

}
