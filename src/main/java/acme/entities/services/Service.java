
package acme.entities.services;

import java.beans.Transient;
import java.time.Year;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.AssertTrue;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Service extends AbstractEntity {

	//Serialisation version ------------------------
	private static final long	serialVersionUID	= 1L;

	//Atributes ------------------------------------

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
	@Column(unique = true)
	@ValidString(pattern = "^[A-Z]{4}-[0-9]{2}$")
	@Automapped
	private String				promotionCode;

	@Optional
	@ValidMoney
	@Automapped
	private Money				discountAmount;

	// Derived attributes -----------------------------------------------------


	@Transient
	@AssertTrue(message = "{validation.experience.promotionCode}")
	public boolean isValidPromotionCode() {
		if (this.promotionCode == null)
			return true;

		String currentYearSuffix = String.valueOf(Year.now().getValue()).substring(2); //obtiene 2 ultimas año
		String codeYearSuffix = this.promotionCode.substring(this.promotionCode.length() - 2); //obtiene 2 ultimas del codigo
		return codeYearSuffix.equals(currentYearSuffix); //compara y devuelve
	}

	// Relationships ----------------------------------------------------------

	//		@ManyToOne(optional = false)
	//		@JoinColumn(name = "airport_id", nullable = false)
	//		private Airport airport; 	//OJO!! Descomentar cuando se implemente la entidad Airport*************

}
