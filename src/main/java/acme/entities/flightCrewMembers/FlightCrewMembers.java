
package acme.entities.flightCrewMembers;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class FlightCrewMembers extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Attributes
	@Mandatory
	@NotBlank
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	@Column(unique = true)
	@Automapped
	private String				employeeCode;

	@Mandatory
	@NotBlank
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				languageSkills;

	@Mandatory
	@NotNull
	@Automapped
	private AvailabilityStatus	availabilityStatus;

	@Mandatory
	@Valid
	@Automapped
	private String				airline;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				salary;

	@Optional
	@Valid
	@Automapped
	private Integer				yearsOfExperience;

	// Relationships
}
