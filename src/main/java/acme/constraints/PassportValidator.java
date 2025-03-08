
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
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {

			String phonePattern = "^[A-Z0-9]{6,9}$";

			if (!passportNumber.matches(phonePattern))
				super.state(context, false, "*", "acme.validation.phone.bad-format-passport.message");

		}

		result = !super.hasErrors(context);

		return result;

	}
}
