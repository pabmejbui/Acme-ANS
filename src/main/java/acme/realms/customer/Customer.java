
package acme.realms.customer;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidIdentifier;
import acme.constraints.ValidPhone;
import acme.constraints.customer.ValidCustomer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidCustomer
public class Customer extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	// Attributes----------------------------------------

	@Mandatory
	@Column(unique = true)
	@ValidIdentifier
	private String				idCustomer;

	@Mandatory
	@ValidPhone
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				physicalAddress;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				city;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				country;

	@Optional
	@ValidNumber(max = 500000)
	@Automapped
	private Integer				earnedPoints;

	// Derived attributes--------------------------------

	// Relationships-------------------------------------

}
