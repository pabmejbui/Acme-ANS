
package acme.constraints;

import java.time.Year;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PromoCodeValidator implements ConstraintValidator<ValidPromoCode, String> {

	// Patrón para validar el código promocional (4 letras mayúsculas + "-" + 2 dígitos)
	private static final String PROMO_CODE_PATTERN = "^[A-Z]{4}-[0-9]{2}$";


	@Override
	public void initialize(final ValidPromoCode constraintAnnotation) {
		// No es necesario inicializar nada
	}

	@Override
	public boolean isValid(final String promoCode, final ConstraintValidatorContext context) {
		if (promoCode == null || promoCode.isBlank())
			return true; // Permitimos valores nulos o vacíos porque es @Optional

		boolean isValidFormat = promoCode.matches(PromoCodeValidator.PROMO_CODE_PATTERN);
		boolean isValidYear = false;

		String currentYearSuffix = String.valueOf(Year.now().getValue()).substring(2);

		if (promoCode.length() > 3) { // Evitar errores de substring
			String codeYearSuffix = promoCode.substring(promoCode.length() - 2);
			isValidYear = codeYearSuffix.equals(currentYearSuffix);
		}

		// Si el formato es incorrecto
		if (!isValidFormat) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.promoCode.bad-format-code.message").addConstraintViolation();
			return false;
		}

		// Si el año es incorrecto
		if (!isValidYear) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.promoCode.invalid-year.message").addConstraintViolation();
			return false;
		}

		return true; // Si pasa ambas validaciones, es válido
	}
}
