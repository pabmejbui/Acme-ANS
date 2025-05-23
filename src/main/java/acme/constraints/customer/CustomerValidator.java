
package acme.constraints.customer;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.customer.Customer;
import acme.realms.customer.CustomerRepository;

@Validator
public class CustomerValidator extends AbstractValidator<ValidCustomer, Customer> {

	@Autowired
	private CustomerRepository	repository;

	private static final String	IDENTIFIER_PATTERN	= "^[A-Z]{2,3}\\d{6}$";
	private static final String	PHONE_PATTERN		= "^\\+?\\d{6,15}$";


	@Override
	protected void initialise(final ValidCustomer annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Customer customer, final ConstraintValidatorContext context) {
		if (customer == null || context == null) {
			super.state(context, false, "customer", "customer.validation.customer.null");
			return false;
		}

		boolean isValid = true;

		// Validación del identificador
		String identifier = customer.getIdCustomer();
		DefaultUserIdentity identity = customer.getUserAccount() != null ? customer.getUserAccount().getIdentity() : null;
		String fullName = identity != null ? identity.getFullName() : null;

		if (identifier == null || identifier.isBlank()) {
			super.state(context, false, "idCustomer", "customer.validation.identifier.not-null");
			isValid = false;
		} else if (!identifier.matches(CustomerValidator.IDENTIFIER_PATTERN)) {
			super.state(context, false, "idCustomer", "customer.validation.identifier.bad-format");
			isValid = false;
		} else {
			// Validación de iniciales
			if (fullName == null || fullName.isBlank()) {
				super.state(context, false, "idCustomer", "customer.validation.fullname.missing");
				isValid = false;
			} else {
				String initials = this.extractInitials(fullName);
				String identifierInitials = identifier.replaceAll("\\d", "");
				if (!identifierInitials.equals(initials)) {
					super.state(context, false, "idCustomer", "customer.validation.identifier.initials-mismatch");
					isValid = false;
				}
			}

			// Validación de unicidad del identificador
			Customer existingCustomer = this.repository.findCustomerIdentifier(identifier);
			boolean isUnique = existingCustomer == null || existingCustomer.equals(customer);
			super.state(context, isUnique, "idCustomer", "acme.validation.customer.duplicated-identifier.message");
			isValid &= isUnique;
		}

		// ✅ Validación del teléfono (siempre se evalúa)
		String phoneNumber = customer.getPhoneNumber();
		if (phoneNumber != null && !phoneNumber.isBlank())
			if (!phoneNumber.matches(CustomerValidator.PHONE_PATTERN)) {
				super.state(context, false, "phoneNumber", "acme.validation.phone.bad-format-phone.message");
				isValid = false;
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
