
package acme.realms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidUrl;
import acme.constraints.manager.ValidManager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidManager
public class Manager extends AbstractRole {

	// Serialisation version

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@Column(unique = true)
	@Automapped
	private String				identifier;

	@Mandatory
	@Automapped
	@ValidNumber(min = 0, max = 120)
	private Integer				experience;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				birthDate;

	@Optional
	@Automapped
	@ValidUrl
	private String				pictureLink;

	// Derived Attributes

	// Relationships

}
