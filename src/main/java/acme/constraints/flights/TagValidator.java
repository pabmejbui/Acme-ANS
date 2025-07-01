package acme.constraints.flights;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class TagValidator extends AbstractValidator<ValidTag, String> {

	@Override
	protected void initialise(final ValidTag annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final String tag, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		if (tag == null || tag.trim().isEmpty()) {
			super.state(context, false, "", "flight.validation.tag.not-blank");
			result = false;
		} else if (tag.length() < 1 || tag.length() > 50) {
			super.state(context, false, "", "flight.validation.tag.length");
			result = false;
		}

		return result;
	}
}
