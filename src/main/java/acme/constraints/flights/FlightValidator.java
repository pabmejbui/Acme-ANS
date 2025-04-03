
package acme.constraints.flights;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.flights.Flight;
import acme.entities.flights.FlightRepository;

@Validator
public class FlightValidator extends AbstractValidator<ValidFlight, Flight> {

	@Autowired
	private FlightRepository repository;


	@Override
	protected void initialise(final ValidFlight annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Flight flight, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;
		String tag = flight.getTag();

		if (tag == null || tag.trim().isEmpty()) {
			super.state(context, false, "tag", "flight.validation.tag.not-blank");
			result = false;
		} else if (tag.length() < 1 || tag.length() > 50) {
			super.state(context, false, "tag", "flight.validation.tag.length");
			result = false;
		} else {
			Flight existing = this.repository.findOneByTag(tag);
			boolean duplicated = existing != null && existing.getId() != flight.getId();

			super.state(context, !duplicated, "tag", "flight.validation.tag.duplicated");
			result = result && !duplicated;
		}

		return result;
	}
}
