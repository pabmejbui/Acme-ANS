
package acme.entities.bookings;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.bookings.ValidBooking;
import acme.entities.flights.Flight;
import acme.realms.customer.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidBooking
@Table(indexes = {
	@Index(columnList = "customer_id"), @Index(columnList = "locatorCode")
})
public class Booking extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Attributes----------------------------------------

	@Mandatory
	@ValidString
	@Column(unique = true)
	private String				locatorCode;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				purchaseMoment;

	@Mandatory
	@Valid
	@Automapped
	private TravelClass			travelClass;

	@Optional
	@ValidString
	@Automapped
	private String				lastCardNibble;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Derived attributes--------------------------------


	@Transient
	public Money getCost() {
		BookingRepository repository = SpringHelper.getBean(BookingRepository.class);
		Double flightPrice = this.flight != null ? this.flight.getCost().getAmount() : 0.00;
		String currency = this.flight != null ? this.flight.getCost().getCurrency() : "EUR";

		Integer passengerCount = repository.countPassengersByLocatorCode(this.locatorCode);
		passengerCount = passengerCount != null ? passengerCount : 0;
		Money bookingCost = new Money();
		bookingCost.setCurrency(currency);
		bookingCost.setAmount(flightPrice * passengerCount);

		return bookingCost;
	}

	// Relationships-------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Customer	customer;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight		flight;

}
