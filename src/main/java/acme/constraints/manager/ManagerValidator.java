
package acme.constraints.manager;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.realms.Manager;

@Validator
public class ManagerValidator extends AbstractValidator<ValidManager, Manager> {

	private static final String IDENTIFIER_PATTERN = "^[A-Z]{2,3}\\d{6}$";


	@Override
	protected void initialise(final ValidManager annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Manager manager, final ConstraintValidatorContext context) {
		assert manager != null && context != null;

		boolean isValid = true;

		// Validación del identifier no nulo y del patrón correcto
		String identifier = manager.getIdentifier();

		if (identifier == null || identifier.isBlank()) {
			super.state(context, false, "identifier", "manager.validation.identifier.not-null");
			isValid = false;

		} else if (!identifier.matches(ManagerValidator.IDENTIFIER_PATTERN)) {
			super.state(context, false, "identifier", "manager.validation.identifier.bad-format");
			isValid = false;

		} else {
			// Validación de las iniciales según el nombre completo
			DefaultUserIdentity identity = manager.getUserAccount().getIdentity();

			if (identity == null || identity.getFullName() == null) {
				super.state(context, false, "identifier", "manager.validation.fullname.missing");
				isValid = false;

			} else {
				String initials = this.extractInitials(identity.getFullName());
				String identifierInitials = identifier.replaceAll("\\d", "");

				if (!identifierInitials.equals(initials)) {
					super.state(context, false, "identifier", "manager.validation.identifier.initials-mismatch");
					isValid = false;
				}
			}
		}

		// Ponemos edad mínima (18 años)
		Date birthDate = manager.getBirthDate();

		if (birthDate == null) {
			super.state(context, false, "birthDate", "manager.validation.birthDate.null");
			isValid = false;

		} else {
			Date minimumAdultDate = MomentHelper.deltaFromCurrentMoment(-18, ChronoUnit.YEARS);
			if (!MomentHelper.isBeforeOrEqual(birthDate, minimumAdultDate)) {
				super.state(context, false, "birthDate", "manager.validation.age.less-than-18");
				isValid = false;
			}
		}

		return isValid && !super.hasErrors(context);
	}

	private String extractInitials(final String fullName) {
		String[] parts = fullName.split(",\\s*");
		if (parts.length < 2)
			return "";

		String surname = parts[0];
		String names = parts[1];

		StringBuilder initials = new StringBuilder();
		initials.append(Character.toUpperCase(surname.charAt(0)));

		String[] nameParts = names.split("\\s+");
		for (int i = 0; i < Math.min(nameParts.length, 2); i++)
			initials.append(Character.toUpperCase(nameParts[i].charAt(0)));

		return initials.toString();
	}
}
