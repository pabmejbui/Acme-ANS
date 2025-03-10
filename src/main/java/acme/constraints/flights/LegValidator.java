
package acme.constraints.flights;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.flights.Leg;

@Validator
public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	@Override
	protected void initialise(final ValidLeg annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {

		assert leg != null && context != null;

		boolean valid = true;

		if (leg.getScheduledDeparture() == null || leg.getScheduledArrival() == null) {
			super.state(context, false, "*", "flight.validation.leg.schedule.null");
			valid = false;
		} else {

			// scheduledArrival debe ser al menos 1 minuto posterior a scheduledDeparture
			Date minValidArrival = MomentHelper.deltaFromMoment(leg.getScheduledDeparture(), 1, ChronoUnit.MINUTES);
			if (leg.getScheduledArrival().before(minValidArrival)) {
				super.state(context, false, "scheduledArrival", "flight.validation.scheduledArrival.min-duration");
				valid = false;
			}
		}

		return valid && !super.hasErrors(context);
	}
}
