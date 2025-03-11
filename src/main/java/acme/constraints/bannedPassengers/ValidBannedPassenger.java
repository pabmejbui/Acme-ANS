
package acme.constraints.bannedPassengers;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({
	ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BannedPassengerValidator.class)
@Documented
public @interface ValidBannedPassenger {

	String message() default "Invalid banned passenger schedule.";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
