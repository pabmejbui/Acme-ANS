
package acme.constraints.aircraft;

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
@Constraint(validatedBy = AircraftValidator.class)
@Documented
public @interface ValidAircraft {

	String message() default "Aircraft validation failed.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
