
package acme.entities.weather;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class WeatherCondition extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	private String				city;
	private String				country;
	private Double				temperature;
	private Double				humidity;
	private Double				windSpeed;
	private String				description;

	@Temporal(TemporalType.TIMESTAMP)
	private Date				reportTime;
}
