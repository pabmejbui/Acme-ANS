
package acme.constraints.flights;

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
@Constraint(validatedBy = LegValidator.class)
@Documented
public @interface ValidLeg {

	String message() default "Invalid leg schedule.";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
