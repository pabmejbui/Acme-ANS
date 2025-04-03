
package acme.constraints.airline;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.airlines.Airline;
import acme.entities.airlines.AirlineRepository;

@Validator
public class AirlineValidator extends AbstractValidator<ValidAirline, Airline> {

	@Autowired
	private AirlineRepository repository;


	@Override
	protected void initialise(final ValidAirline annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Airline airline, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;
		String iataCode = airline.getIataCode();

		if (iataCode == null || iataCode.trim().isEmpty()) {
			super.state(context, false, "iataCode", "airline.validation.iataCode.not-blank");
			result = false;
		} else {
			Airline existing = this.repository.findOneByIataCode(iataCode);
			boolean duplicated = existing != null && existing.getId() != airline.getId();

			super.state(context, !duplicated, "iataCode", "airline.validation.iataCode.duplicated");
			result = result && !duplicated;
		}

		if (airline.getFoundationMoment() != null) {
			final boolean validMoment = MomentHelper.isPresentOrPast(airline.getFoundationMoment());
			super.state(context, validMoment, "foundationMoment", "airline.validation.foundation.future");
			result = result && validMoment;
		}

		return result;
	}
}
