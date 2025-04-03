
package acme.constraints.aircraft;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.aircrafts.Aircraft;
import acme.entities.aircrafts.AircraftRepository;

@Validator
public class AircraftValidator extends AbstractValidator<ValidAircraft, Aircraft> {

	@Autowired
	private AircraftRepository repository;


	@Override
	protected void initialise(final ValidAircraft constraintAnnotation) {
		assert constraintAnnotation != null;
	}

	@Override
	public boolean isValid(final Aircraft aircraft, final ConstraintValidatorContext context) {
		assert context != null;

		if (aircraft == null || aircraft.getRegistrationNumber() == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			return false;
		}

		final String registration = aircraft.getRegistrationNumber();
		final Aircraft existing = this.repository.findAircraftByRegistrationNumber(registration);

		final boolean unique = existing == null || existing.getId() == aircraft.getId();
		super.state(context, unique, "registrationNumber", "aircraft.validation.registration.duplicate");

		return unique;
	}
}
