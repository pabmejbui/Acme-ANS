
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import acme.entities.flights.LegStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	int							rankingBasedOneExperience;
	int							yearsToRetire;
	double						ratioOnTimeLegs;
	double						ratioDelayedLegs;

	List<String>				mostPopularAirports;
	List<String>				lessPopularAirports;

	Map<LegStatus, Integer>		numberOfLegsGroupedByStatus;

	Money						avgDeviationOfCost;
	Money						minDeviationOfCost;
	Money						maxDeviationOfCost;
	Money						standardDeviationOfCost;

}
