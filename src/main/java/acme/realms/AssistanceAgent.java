
package acme.realms;

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
import acme.constraints.ValidEmployeeCode;
import acme.entities.airlines.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidEmployeeCode

public class AssistanceAgent extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	//Attributes
	@Mandatory
	@ValidEmployeeCode
	@Column(unique = true)
	private String				employeeCode;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				languages;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				beginDate;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				bio;

	@Optional
	@ValidMoney
	@Automapped
	private Money				salary;

	@Optional
	@ValidUrl
	@Automapped
	private String				photoLink;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Airline				airline;

}
