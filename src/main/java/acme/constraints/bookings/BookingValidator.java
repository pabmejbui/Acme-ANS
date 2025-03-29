
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
	private BookingRepository bookingRepository;


	@Override
	protected void initialise(final ValidBooking annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Booking booking, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		if (booking == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean uniqueLocator;
			Booking existingBooking;

			existingBooking = this.bookingRepository.findBookingByLocatorCode(booking.getLocatorCode());
			uniqueLocator = existingBooking == null || existingBooking.equals(booking);

			super.state(context, uniqueLocator, "locatorCode", "acme.validation.booking.duplicate-locator.message");

			if (booking.getPurchaseMoment() != null) {
				boolean isBeforeNow = MomentHelper.isPresentOrPast(booking.getPurchaseMoment());
				super.state(context, isBeforeNow, "purchaseMoment", "acme.validation.booking.purchase-moment-past.message");
			}

		}

		result = !super.hasErrors(context);

		return result;
	}
}
