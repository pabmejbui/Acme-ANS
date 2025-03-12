
package acme.entities.flights;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.realms.Manager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	// Serialisation version

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Column(unique = true)
	private String				tag;

	@Mandatory
	@Valid
	@Automapped
	private Indication			indication;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				cost;

	@Optional
	@ValidString
	@Automapped
	private String				description;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Derived attributes


	@Transient
	public Date getScheduledDeparture() {
		Date result;
		FlightRepository repository;
		repository = SpringHelper.getBean(FlightRepository.class);
		result = repository.findScheduledDeparture(this.getId());
		return result;
	}

	@Transient
	public Date getScheduledArrival() {
		Date result;
		FlightRepository repository;
		repository = SpringHelper.getBean(FlightRepository.class);
		result = repository.findScheduledArrival(this.getId());
		return result;
	}

	@Transient
	public String getOriginCity() {
		String result;
		Date departure;
		FlightRepository repository;
		repository = SpringHelper.getBean(FlightRepository.class);
		departure = repository.findScheduledDeparture(this.getId());
		result = repository.findOriginCity(this.getId(), departure);
		return result;
	}

	@Transient
	public String getDestinationCity() {
		String result;
		Date arrival;
		FlightRepository repository;
		repository = SpringHelper.getBean(FlightRepository.class);
		arrival = repository.findScheduledArrival(this.getId());
		result = repository.findDestinationCity(this.getId(), arrival);
		return result;
	}

	@Transient
	public int getNumberOfLayovers() {
		int result = 0;
		FlightRepository repository;
		repository = SpringHelper.getBean(FlightRepository.class);
		result = repository.countLayovers(this.getId());
		return result;
	}

	@Transient
	public boolean isAvailable() {
		boolean result;
		FlightRepository repository;
		repository = SpringHelper.getBean(FlightRepository.class);
		result = !this.draftMode && repository.publishedLegs(this.getId()) != null;

		return result;
	}

	// Relationships


	@Mandatory
	@ManyToOne(optional = false)
	@Automapped
	private Manager manager;

}
