
package acme.entities.recomendations;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Recommendation extends AbstractEntity {

	// Serialisation version ------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes ------------------------------------

	@Mandatory
	@ValidString(max = 100)
	@Automapped
	private String				title;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				description;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				location;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				city;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				country;

	@Optional
	@Automapped
	private Double				latitude;

	@Optional
	@Automapped
	private Double				longitude;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				category;

	@Optional
	@ValidUrl
	@Automapped
	private String				url;

	@Optional
	@Automapped
	private Double				rating;

	@Optional
	@ValidUrl
	@Automapped
	private String				imageUrl;

	@Optional
	@ValidString(max = 50)
	@Automapped
	private String				priceRange;

	@Mandatory
	@ValidString(max = 10)
	@Automapped
	private String				airportCode;
}
