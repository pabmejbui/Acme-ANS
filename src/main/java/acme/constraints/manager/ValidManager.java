
package acme.constraints.manager;

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
@Constraint(validatedBy = ManagerValidator.class)
@Documented
public @interface ValidManager {

	String message() default "Manager validation failed.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
