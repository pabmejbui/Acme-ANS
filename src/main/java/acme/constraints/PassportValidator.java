
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class PassportValidator extends AbstractValidator<ValidPassport, String> {

	//Internal state ------------------------------------------------------------

	//ConstraintValidator interface ------------------------------------------------------------

	@Override
	protected void initialise(final ValidPassport annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final String passportNumber, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (passportNumber == null)
			super.state(context, false, "passportNumber", "javax.validation.constraints.NotNull.message");
		else {

			String passportPattern = "^[A-Z0-9]{6,9}$";

			if (!passportNumber.matches(passportPattern))
				super.state(context, false, "passportNumber", "acme.validation.passport.bad-format-passport.message");

		}

		result = !super.hasErrors(context);

		return result;

	}
}
