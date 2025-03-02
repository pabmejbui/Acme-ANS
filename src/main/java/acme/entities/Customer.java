
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Attributes----------------------------------------

	@NotBlank
	@ValidString(pattern = "^[A-Z]{2-3}\\d{6}$")
	@Column(unique = true)
	@Automapped
	@Mandatory
	private String				idCustomer;

	@Mandatory
	@NotBlank
	@ValidString(pattern = "^\\+?\\d{6,15}$")
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

	//	@Transient
	//	@AssertTrue(message = "{validation.customer.idCustomer}")
	//	public boolean isValidIdCustomer() {
	//		if (this.idCustomer == null)
	//			return true;
	//
	//		String customer = this.idCustomer;
	//		String initialCustomer = String.valueOf(name.charAt(0)) + String.valueOf(surname1.charAt(0));
	//		return customer.equals(initialCustomer); //compara y devuelve
	//	}

	// Relationships-------------------------------------

}
