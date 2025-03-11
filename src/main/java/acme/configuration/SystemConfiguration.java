
package acme.configuration;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidCurrency;
import acme.constraints.configuration.ValidAcceptedCurrencies;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SystemConfiguration extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@ValidCurrency
	@Mandatory
	@Automapped
	protected String			currency;

	@ValidAcceptedCurrencies
	@Mandatory
	@Automapped
	protected String			acceptedCurrencies;
}
