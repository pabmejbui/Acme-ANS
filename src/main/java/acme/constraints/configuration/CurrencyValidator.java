
package acme.constraints.configuration;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;

public class CurrencyValidator extends AbstractValidator<ValidCurrency, String> {

	private static final String CURRENCY_REGEX = "^[A-Z]{3}$";


	@Override
	protected void initialise(final ValidCurrency annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final String currency, final ConstraintValidatorContext context) {
		assert context != null;

		if (currency == null || currency.isBlank()) {
			super.state(context, false, "*", "javax.validation.constraints.NotBlank.message");
			return false;
		}

		boolean matchesPattern = currency.matches(CurrencyValidator.CURRENCY_REGEX);
		super.state(context, matchesPattern, "*", "acme.validation.currency.invalid");

		return matchesPattern && !super.hasErrors(context);
	}
}
