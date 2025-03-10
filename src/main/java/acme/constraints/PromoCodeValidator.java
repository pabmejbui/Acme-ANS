
package acme.constraints;

import java.time.Year;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;

public class PromoCodeValidator extends AbstractValidator<ValidPromoCode, String> {

	private static final String PROMO_CODE_PATTERN = "^[A-Z]{4}-[0-9]{2}$";


	@Override
	protected void initialise(final ValidPromoCode annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final String promoCode, final ConstraintValidatorContext context) {

		assert context != null;
		boolean result;

		if (promoCode == null || promoCode.isBlank())
			return true;

		if (!promoCode.matches(PromoCodeValidator.PROMO_CODE_PATTERN))
			super.state(context, false, "*", "acme.validation.promoCode.bad-format-code.message");

		String currentYearSuffix = String.valueOf(Year.now().getValue()).substring(2);
		String codeYearSuffix = promoCode.substring(promoCode.length() - 2);

		if (!codeYearSuffix.equals(currentYearSuffix))
			super.state(context, false, "*", "acme.validation.promoCode.invalid-year.message");

		result = !super.hasErrors(context);

		return result;

	}

}
