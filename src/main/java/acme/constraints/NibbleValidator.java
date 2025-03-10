
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class NibbleValidator extends AbstractValidator<ValidNibble, Integer> {

	@Override
	protected void initialise(final ValidNibble annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Integer nibbleCard, final ConstraintValidatorContext context) {

		assert context != null;

		String nibbleStr = String.valueOf(nibbleCard);

		if (!(nibbleStr.length() == 4 || nibbleStr.matches("\\d{4}")))
			super.state(context, false, "*", "acme.validation.card-nibble.bad-format.message");

		return !super.hasErrors(context);
	}

}
