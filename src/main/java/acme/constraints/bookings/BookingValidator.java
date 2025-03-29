
package acme.constraints.bookings;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRepository;

@Validator
public class BookingValidator extends AbstractValidator<ValidBooking, Booking> {

	@Autowired
	private BookingRepository bookingRepository;


	@Override
	protected void initialise(final ValidBooking annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Booking booking, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		if (booking.isDraftMode() == false) {

			// Validar si el locatorCode ya existe
			List<String> existingLocatorCodes = this.bookingRepository.findAllLocatorCodes();
			boolean isDuplicate = existingLocatorCodes.contains(booking.getLocatorCode());

			if (isDuplicate)
				super.state(context, false, "locatorCode", "acme.validation.booking.duplicate-locator.message");

			int passengerCount = this.bookingRepository.countPassengersByLocatorCode(booking.getLocatorCode());
			boolean hasPassengers = passengerCount > 0;
			super.state(context, hasPassengers, "*", "acme.validation.booking.passenger-required.message");

		}

		result = !super.hasErrors(context);
		return result;
	}

}
