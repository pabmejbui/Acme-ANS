
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class LicenseValidator extends AbstractValidator<ValidLicense, String> {

	//Internal state ------------------------------------------------------------

	//ConstraintValidator interface ------------------------------------------------------------

	@Override
	protected void initialise(final ValidLicense annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final String licenseNumber, final ConstraintValidatorContext context) {

		assert context != null;

		if (licenseNumber == null || licenseNumber.isBlank())
			return true;

		String licensePattern = "^[A-Z]{2-3}\\\\d{6}$";

		if (!licenseNumber.matches(licensePattern))
			super.state(context, false, "*", "acme.validation.license.bad-format.message");

		return !super.hasErrors(context);

	}
}
