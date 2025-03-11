
package acme.constraints.configuration;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;

public class AcceptedCurrenciesValidator extends AbstractValidator<ValidAcceptedCurrencies, String> {

	private static final String ACCEPTED_CURRENCIES_REGEX = "^([A-Z]{3}(,\\s?|$))+$";


	@Override
	protected void initialise(final ValidAcceptedCurrencies annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final String currencies, final ConstraintValidatorContext context) {
		assert context != null;

		if (currencies == null || currencies.isBlank()) {
			super.state(context, false, "*", "javax.validation.constraints.NotBlank.message");
			return false;
		}

		boolean matchesPattern = currencies.matches(AcceptedCurrenciesValidator.ACCEPTED_CURRENCIES_REGEX);
		super.state(context, matchesPattern, "*", "acme.validation.acceptedCurrencies.invalid");

		return matchesPattern && !super.hasErrors(context);
	}
}
