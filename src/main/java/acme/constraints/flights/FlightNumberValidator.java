
package acme.constraints.flights;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.flights.FlightRepository;
import acme.entities.flights.Leg;

@Validator
public class FlightNumberValidator extends AbstractValidator<ValidFlightNumber, Leg> {

	@Autowired
	private FlightRepository repository;


	@Override
	protected void initialise(final ValidFlightNumber annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		if (leg == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			result = false;
		} else {
			String flightNumber = leg.getFlightNumber();
			String airlineIataCode = leg.getAirline().getIataCode();

			// Validación del formato del número de vuelo
			String pattern = "^" + airlineIataCode + "\\d{4}$";
			boolean validFormat = flightNumber.matches(pattern);

			super.state(context, validFormat, "flightNumber", "acme.validation.manager.leg.flight-number.format.message");
			result = result && validFormat;

			// Validación de unicidad del número de vuelo
			Leg existingLeg = this.repository.findLegByFlightNumber(flightNumber);
			boolean uniqueFlightNumber = existingLeg == null || existingLeg.equals(leg);

			super.state(context, uniqueFlightNumber, "flightNumber", "acme.validation.manager.leg.flight-number.duplicate.message");
			result = result && uniqueFlightNumber;
		}

		return result;
	}
}
