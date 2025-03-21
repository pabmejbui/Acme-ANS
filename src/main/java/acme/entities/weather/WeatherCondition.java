
package acme.entities.weather;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class WeatherCondition extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	private String				city;

	@Mandatory
	@ValidString(max = 50)
	private String				country;

	@Mandatory
	@ValidNumber(integer = 2, fraction = 2)
	private Double				temperature;

	@Mandatory
	@ValidNumber(min = 0, max = 100)
	private Double				humidity;

	@Mandatory
	@ValidNumber(min = 0, integer = 3, fraction = 2)
	private Double				windSpeed;

	@Mandatory
	@ValidString
	private String				description;

	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment
	private Date				reportTime;
}
