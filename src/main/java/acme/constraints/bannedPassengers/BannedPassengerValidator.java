
package acme.constraints.bannedPassengers;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.MomentHelper;
import acme.entities.bannedPassengers.BannedPassenger;

public class BannedPassengerValidator extends AbstractValidator<ValidBannedPassenger, BannedPassenger> {

	@Override
	protected void initialise(final ValidBannedPassenger annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final BannedPassenger bp, final ConstraintValidatorContext context) {

		assert bp != null && context != null;

		boolean valid = true;

		if (bp.getBanIssuedDate() == null) {
			super.state(context, false, "*", "banned.passenger.validation..ban.issued.date.null");
			valid = false;
		} else {

			// liftDate debe ser al menos 1 minuto posterior a banIssuedDate
			Date minBanIssuedDate = MomentHelper.deltaFromMoment(bp.getBanIssuedDate(), 1, ChronoUnit.MINUTES);
			if (bp.getLiftDate().before(minBanIssuedDate)) {
				super.state(context, false, "liftDate", "banned.passenger.validation.lift.date.min-duration");
				valid = false;
			}
		}

		return valid && !super.hasErrors(context);
	}

}
