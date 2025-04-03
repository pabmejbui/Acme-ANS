
package acme.features.manager.managerDashboard;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.configuration.SystemConfiguration;
import acme.forms.ManagerDashboard;
import acme.realms.manager.Manager;

@GuiService
public class ManagerDashboardShowService extends AbstractGuiService<Manager, ManagerDashboard> {

	@Autowired
	private ManagerDashboardRepository repository;


	@Override
	public void authorise() {
		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Manager manager = this.repository.findManagerById(managerId);
		boolean status = manager != null && super.getRequest().getPrincipal().hasRealm(manager);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		Integer ranking = this.repository.findRankingByManagerId(managerId);
		Integer yearsToRetire = this.repository.findYearsToRetire(managerId);

		int onTime = this.repository.countOnTimeLegs(managerId);
		int delayed = this.repository.countDelayedLegs(managerId);
		int total = onTime + delayed;

		double ratioOnTime = total > 0 ? (double) onTime / total : 0.0;
		double ratioDelayed = total > 0 ? (double) delayed / total : 0.0;

		Object[] costStatsRaw = this.repository.statsCost(managerId).get(0);
		Double avgAmount = (Double) costStatsRaw[0];
		Double minAmount = (Double) costStatsRaw[1];
		Double maxAmount = (Double) costStatsRaw[2];
		Double stdAmount = (Double) costStatsRaw[3];

		SystemConfiguration config = this.repository.getSystemConfiguration();
		String currency = config.getCurrency();

		Money avg = new Money();
		avg.setCurrency(currency);
		avg.setAmount(avgAmount);

		Money min = new Money();
		min.setCurrency(currency);
		min.setAmount(minAmount);

		Money max = new Money();
		max.setCurrency(currency);
		max.setAmount(maxAmount);

		Money std = new Money();
		std.setCurrency(currency);
		std.setAmount(stdAmount);

		ManagerDashboard dashboard = new ManagerDashboard();
		dashboard.setRankingBasedOneExperience(ranking);
		dashboard.setYearsToRetire(yearsToRetire);
		dashboard.setRatioOnTimeLegs(ratioOnTime);
		dashboard.setRatioDelayedLegs(ratioDelayed);
		dashboard.setAvgDeviationOfCost(avg);
		dashboard.setMinDeviationOfCost(min);
		dashboard.setMaxDeviationOfCost(max);
		dashboard.setStandardDeviationOfCost(std);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ManagerDashboard object) {
		Dataset dataset = super.unbindObject(object, //
			"rankingBasedOneExperience", "yearsToRetire", //
			"ratioOnTimeLegs", "ratioDelayedLegs", //
			"avgDeviationOfCost", "minDeviationOfCost", //
			"maxDeviationOfCost", "standardDeviationOfCost");

		super.getResponse().addData(dataset);
	}
}
