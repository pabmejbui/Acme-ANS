
package acme.entities.aircrafts;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.aircraft.ValidAircraft;
import acme.entities.airlines.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidAircraft
public class Aircraft extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Automapped
	@Mandatory
	@ValidString(max = 50)
	private String				model;

	@Mandatory
	@Column(unique = true)
	@ValidString(max = 50)
	private String				registrationNumber;

	@Automapped
	@Mandatory
	@ValidNumber
	private Integer				capacity;

	@Automapped
	@Mandatory
	@ValidNumber(min = 2000, max = 50000)
	private Integer				cargoWeight;

	@Automapped
	@Mandatory
	@Valid
	private AircraftStatus		status;

	@Automapped
	@Optional
	@ValidString
	private String				details;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Derived attributes

	// Relationships

	@Valid
	@Mandatory
	@ManyToOne(optional = false)
	private Airline				airline;

}
