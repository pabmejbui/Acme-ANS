
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class PhoneValidator extends AbstractValidator<ValidPhone, String> {

	//Internal state ------------------------------------------------------------

	//ConstraintValidator interface ------------------------------------------------------------

	@Override
	protected void initialise(final ValidPhone annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final String phoneNumber, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (phoneNumber == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {

			String phonePattern = "^\\+?\\d{6,15}$";

			if (!phoneNumber.matches(phonePattern))
				super.state(context, false, "*", "acme.validation.phone.bad-format-phone.message");

		}

		result = !super.hasErrors(context);

		return result;

	}
}
