
package acme.entities.recomendations;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Recommendation extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 100)
	@Automapped
	private String				name;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				description;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				category;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				location;

	// NUEVOS CAMPOS PARA LATITUD Y LONGITUD
	@Optional
	private Double				latitude;

	@Optional
	private Double				longitude;

}
