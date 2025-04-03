
package acme.forms;

import acme.client.components.basis.AbstractForm;

public class AssistanceAgentDashboards extends AbstractForm {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	double						ratioClaimsResolved;

	double						ratioClaimsRejected;

	String						topMonth1;
	String						topMonth2;
	String						topMonth3;

	//The average, minimum, maximum, and standard deviation of the number of logs their claims have.
	double						avgLogsPerClaim;
	int							minLogsPerClaim;
	int							maxLogsPerClaim;
	double						stdDeviationLogsPerClaim;

	//The average, minimum, maximum, and standard deviation of the number of claims they assisted during the last month.
	double						avgClaimsLastMonth;
	int							minClaimsLastMonth;
	int							maxClaimsLastMonth;
	double						stdDeviationClaimsLastMonth;

}
