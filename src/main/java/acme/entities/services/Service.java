
package acme.entities.services;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidPromoCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Service extends AbstractEntity {

	//Serialisation version ------------------------
	private static final long	serialVersionUID	= 1L;

	//Attributes ------------------------------------

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				name;

	@Mandatory
	@ValidUrl
	@Automapped
	private String				pictureUrl;

	@Mandatory
	@ValidNumber
	@Automapped
	private Double				averageDwellTime;

	@Optional
	@Column(unique = true, nullable = true)
	@ValidPromoCode
	private String				promotionCode;

	@Optional
	@ValidMoney
	@Automapped
	private Money				discountAmount;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------


	public void setPromotionCode(final String promotionCode) {
		// AL ser unico el atributo PromotionCode se setea a NULL
		this.promotionCode = promotionCode == null || promotionCode.isBlank() ? null : promotionCode;
	}

	// Relationships ----------------------------------------------------------
}
