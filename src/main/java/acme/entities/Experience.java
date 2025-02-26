
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Experience extends AbstractEntity {

	//Serialisation version ------------------------
	private static final long	serialVersionUID	= 1L;

	//Atributes ------------------------------------

	@Mandatory
	@NotBlank
	@Size(max = 50)
	@Automapped
	private String				name;

	@Mandatory
	@ValidUrl
	@NotBlank
	@Automapped
	private String				pictureUrl;

	@Mandatory
	@NotNull
	@Automapped
	private Double				averageDwellTime;

	@Optional
	@Column(unique = true, nullable = true) //Preguntar si es redundate
	@Pattern(regexp = "^[A-Z]{4}-[0-9]{2}$", message = "{validation.experience.promotionCode}")
	@Automapped
	private String				promotionCode;

	@Optional
	@ValidMoney
	@Automapped
	private Money				discountAmount;
}
