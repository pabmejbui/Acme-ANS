
package acme.constraints.flights;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.flights.Leg;

@Validator
public class FlightNumberValidator extends AbstractValidator<ValidFlightNumber, Leg> {

	private static final String FLIGHT_NUMBER_PATTERN = "^[A-Z]{3}\\d{4}$";


	@Override
	protected void initialise(final ValidFlightNumber annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		assert leg != null && context != null;

		boolean valid = true;
		String flightNumber = leg.getFlightNumber();

		if (flightNumber == null || flightNumber.isBlank()) {
			super.state(context, false, "flightNumber", "flight.validation.flightNumber.not-null");
			return false;
		}

		if (!flightNumber.matches(FlightNumberValidator.FLIGHT_NUMBER_PATTERN)) {
			super.state(context, false, "flightNumber", "flight.validation.flightNumber.bad-format");
			valid = false;
		}

		String airlineIataCode = null;
		if (leg.getFlight() == null || leg.getFlight().getManager() == null || leg.getAirline() == null || leg.getAirline().getIataCode() == null) {

			super.state(context, false, "flightNumber", "flight.validation.flightNumber.missing-airline");
			valid = false;
		} else
			airlineIataCode = leg.getAirline().getIataCode();

		if (valid && airlineIataCode != null) {
			String expectedPattern = "^" + airlineIataCode + "\\d{4}$";
			if (!flightNumber.matches(expectedPattern)) {
				super.state(context, false, "flightNumber", "flight.validation.flightNumber.mismatch");
				valid = false;
			}
		}

		return valid && !super.hasErrors(context);
	}
}
