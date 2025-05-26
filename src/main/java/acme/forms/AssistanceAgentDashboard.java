
package acme.forms;

import java.util.Date;
import java.util.List;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssistanceAgentDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private Double				successfulClaimsRate;
	private Double				rejectedClaimsRate;

	private List<Date>			highestClaimMonths;

	private Double				averageTotalClaims;
	private Integer				minimumTotalClaims;
	private Integer				maximumTotalClaims;
	private Double				stdDevTotalClaims;

	private Double				averageMonthlyAssistedClaims;
	private Integer				minimumMonthlyAssistedClaims;
	private Integer				maximumMonthlyAssistedClaims;
	private Double				stdDevMonthlyAssistedClaims;

}
