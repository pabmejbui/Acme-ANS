
package acme.constraints.passenger;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.passenger.Passenger;

@Validator
public class PassengerValidator extends AbstractValidator<ValidPassenger, Passenger> {

	private static final String PASSPORT_PATTERN = "^[A-Z0-9]{6,9}$";


	@Override
	protected void initialise(final ValidPassenger annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Passenger passenger, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		String passportNumber = passenger.getPassportNumber();
		if (passportNumber != null && !passportNumber.matches(PassengerValidator.PASSPORT_PATTERN)) {
			super.state(context, false, "passportNumber", "acme.validation.passportNumber.notPattern.message");
			result = false;
		}

		return result && !super.hasErrors(context);
	}
}
