
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flights.Leg;

@Validator
public class ActivityLogValidator extends AbstractValidator<ValidActivityLog, ActivityLog> {

	@Override
	protected void initialise(final ValidActivityLog constraintAnnotation) {
		assert constraintAnnotation != null;
	}

	@Override
	public boolean isValid(final ActivityLog log, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;
		if (log == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			Leg leg = log.getFlightAssignment().getLeg();
			Date endMoment = leg.getScheduledArrival();
			Date registrationMoment = log.getRegistrationMoment();
			Date now = MomentHelper.getCurrentMoment();
			boolean isAfter = registrationMoment.after(endMoment) && !registrationMoment.after(now);

			super.state(context, isAfter, "registrationMoment", "acme.validation.log.registration-moment.message");
		}

		result = !super.hasErrors(context);
		return result;
	}
}
