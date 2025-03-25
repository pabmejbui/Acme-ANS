
package acme.features.recommendation;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
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
	@ValidString
	@Automapped
	private String				xid;

	@Mandatory
	@ValidString
	@Automapped
	private String				name;

	@Mandatory
	@ValidNumber(min = 0)
	@Automapped
	private double				dist;

	@Mandatory
	@ValidNumber(min = 0)
	@Automapped
	private int					rate;

	@Mandatory
	@ValidString
	@Automapped
	private String				wikidata;

	@Mandatory
	@ValidString
	@Automapped
	private String				osm;

	@Mandatory
	@ValidString
	@Automapped
	private String				kinds;

	//Relationship ---------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport				airport;

}
