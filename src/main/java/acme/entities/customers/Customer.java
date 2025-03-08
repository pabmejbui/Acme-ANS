
package acme.entities.customers;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidPhone;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Attributes----------------------------------------

	@Mandatory
	@NotBlank
	@ValidString(pattern = "^[A-Z]{2-3}\\d{6}$")
	@Column(unique = true)
	@Automapped
	private String				idCustomer;

	@Mandatory
	@NotBlank
	@ValidPhone
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@NotBlank
	@ValidString(max = 255)
	@Automapped
	private String				physicalAddress;

	@Mandatory
	@NotBlank
	@ValidString(max = 50)
	@Automapped
	private String				city;

	@Mandatory
	@NotBlank
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
