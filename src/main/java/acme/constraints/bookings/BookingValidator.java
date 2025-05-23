
package acme.constraints.bookings;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRepository;

@Validator
public class BookingValidator extends AbstractValidator<ValidBooking, Booking> {

	@Autowired
	private BookingRepository	bookingRepository;
	private static final String	LOCATOR_PATTERN		= "^[A-Z0-9]{6,8}$";
	private static final String	LAST_NIBBLE_PATTERN	= "^[0-9]{4}$";


	@Override
	protected void initialise(final ValidBooking annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Booking booking, final ConstraintValidatorContext context) {
		if (context == null)
			return false;

		boolean result = true;

		if (booking == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			return false;
		}

		String locator = booking.getLocatorCode();

		// Validación de formato del locatorCode
		if (locator == null || locator.isBlank()) {
			super.state(context, false, "locatorCode", "javax.validation.constraints.NotBlank.message");
			result = false;
		} else if (!locator.matches(BookingValidator.LOCATOR_PATTERN)) {
			super.state(context, false, "locatorCode", "acme.validation.booking.locator-code.message");
			result = false;
		}

		// Validación de unicidad del locatorCode
		Booking existingBooking = this.bookingRepository.findBookingByLocatorCode(locator);
		boolean isUnique = existingBooking == null || existingBooking.equals(booking);
		super.state(context, isUnique, "locatorCode", "acme.validation.booking.duplicate-locator.message");

		// Validación del purchaseMoment (debe estar en el pasado o presente)
		if (booking.getPurchaseMoment() != null) {
			boolean isBeforeNow = MomentHelper.isPresentOrPast(booking.getPurchaseMoment());
			super.state(context, isBeforeNow, "purchaseMoment", "acme.validation.booking.purchase-moment-past.message");
		}

		String nibble = booking.getLastCardNibble();
		if (nibble != null && !nibble.isBlank() && !nibble.matches(BookingValidator.LAST_NIBBLE_PATTERN)) {
			super.state(context, false, "lastCardNibble", "acme.validation.booking.lastNibble.notPattern.message");
			result = false;
		}

		return result && !super.hasErrors(context);
	}
}
