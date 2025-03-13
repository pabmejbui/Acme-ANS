
package acme.forms;

import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.entities.airlines.AirlineType;
import acme.entities.airports.OperationalScope;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministratorDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------
	private static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	Map<OperationalScope, Integer>	totalAirportsByOperationalScope;
	Map<AirlineType, Integer>		numberOfAirlinesByType;

	Double							ratioAirlinesWithEmailAndPhone;

	Double							ratioActiveAircrafts;
	Double							ratioNonActiveAircrafts;

	Double							ratioReviewsAboveFive;

	Integer							countReviewsLast10Weeks;
	Double							averageReviewsLast10Weeks;
	Integer							minReviewsLast10Weeks;
	Integer							maxReviewsLast10Weeks;
	Double							standardDeviationReviewsLast10Weeks;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}
