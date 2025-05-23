
package acme.constraints.customer;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.customer.Customer;

@Validator
public class CustomerValidator extends AbstractValidator<ValidCustomer, Customer> {

	private static final String IDENTIFIER_PATTERN = "^[A-Z]{2,3}\\d{6}$";


	@Override
	protected void initialise(final ValidCustomer annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Customer customer, final ConstraintValidatorContext context) {
		if (customer == null) {
			super.state(context, false, "customer", "customer.validation.customer.null");
			return false;
		}
		if (context == null)
			return false;

		boolean isValid = true;
		String identifier = customer.getIdCustomer();

		// Validación de formato del identificador
		if (identifier == null || identifier.isBlank()) {
			super.state(context, false, "idCustomer", "customer.validation.identifier.not-null");
			isValid = false;
		} else if (!identifier.matches(CustomerValidator.IDENTIFIER_PATTERN)) {
			super.state(context, false, "idCustomer", "customer.validation.identifier.bad-format");
			isValid = false;
		}

		// Validación de iniciales usando DefaultUserIdentity
		if (customer.getUserAccount() == null || customer.getUserAccount().getIdentity() == null) {
			super.state(context, false, "idCustomer", "customer.validation.fullname.missing");
			isValid = false;
		} else {
			DefaultUserIdentity identity = customer.getUserAccount().getIdentity();

			if (identity.getFullName() == null || identity.getFullName().isBlank()) {
				super.state(context, false, "idCustomer", "customer.validation.fullname.missing");
				isValid = false;
			} else {
				String initials = this.extractInitials(identity.getFullName());
				String identifierInitials = identifier != null ? identifier.replaceAll("\\d", "") : "";

				if (!identifierInitials.equals(initials)) {
					super.state(context, false, "idCustomer", "customer.validation.identifier.initials-mismatch");
					isValid = false;
				}
			}
		}

		return isValid && !super.hasErrors(context);
	}

	private String extractInitials(final String fullName) {
		if (fullName == null || fullName.isBlank())
			return "";

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
