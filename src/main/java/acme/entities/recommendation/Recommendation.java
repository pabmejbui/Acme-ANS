
package acme.entities.recommendation;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Recommendation extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	//Attributes -------------------------------------------------------
	@Mandatory
	@ValidString
	@Automapped
	private String				city;

	@Mandatory
	@ValidString
	@Automapped
	private String				name;

	@Mandatory
	@ValidNumber
	@Automapped
	private Double				rating;

	@Mandatory
	@ValidString
	@Automapped
	private String				type;

	//Relationship ---------------------------------------------------------

}
