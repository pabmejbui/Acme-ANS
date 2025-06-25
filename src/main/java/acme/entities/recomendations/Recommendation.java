
package acme.entities.recomendations;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.entities.airports.Airport;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Recommendation extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	//Attributes -------------------------------------------------------

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				name;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				category;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				address;

	@Optional
	@ValidString(max = 50)
	@Automapped
	private String				city;

	@Optional
	@ValidString(max = 50)
	@Automapped
	private String				country;

	@Optional
	@ValidUrl
	@Automapped
	private String				website;

	@Optional
	@Automapped
	private Double				latitude;

	@Optional
	@Automapped
	private Double				longitude;

	//Relationship ---------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport				airport;

}
