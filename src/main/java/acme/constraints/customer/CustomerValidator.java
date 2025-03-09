
package acme.constraints.customer;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.Customer;

@Validator
public class CustomerValidator extends AbstractValidator<ValidCustomer, Customer> {

	private static final String IDENTIFIER_PATTERN = "^[A-Z]{2,3}\\d{6}$";


	@Override
	protected void initialise(final ValidCustomer annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Customer customer, final ConstraintValidatorContext context) {
		assert customer != null && context != null;

		boolean isValid = true;

		// Validación del identifier no nulo y del patrón correcto
		String idCustomer = customer.getIdCustomer();

		if (idCustomer == null || idCustomer.isBlank()) {
			super.state(context, false, "idCustomer", "customer.validation.identifier.not-null");
			isValid = false;

		} else if (!idCustomer.matches(CustomerValidator.IDENTIFIER_PATTERN)) {
			super.state(context, false, "idCustomer", "customer.validation.identifier.bad-format");
			isValid = false;

		} else {
			// Validación de las iniciales según el nombre completo
			DefaultUserIdentity identity = customer.getUserAccount().getIdentity();

			if (identity == null || identity.getFullName() == null) {
				super.state(context, false, "idCustomer", "customer.validation.fullname.missing");
				isValid = false;

			} else {
				String initials = this.extractInitials(identity.getFullName());
				String identifierInitials = idCustomer.replaceAll("\\d", "");

				if (!identifierInitials.equals(initials)) {
					super.state(context, false, "idCustomer", "customer.validation.identifier.initials-mismatch");
					isValid = false;
				}
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
