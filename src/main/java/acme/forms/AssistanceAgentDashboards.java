
package acme.forms;

import acme.client.components.basis.AbstractForm;

public class AssistanceAgentDashboards extends AbstractForm {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	//The ratio of claims that have been resolved successfully.
	double						ratioClaimsResolved;

	//The ratio of claims that have been rejected.
	double						ratioClaimsRejected;

	//The top three months with the highest number of claims.
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
