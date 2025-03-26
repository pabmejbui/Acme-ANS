
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PromoCodeValidator.class)
public @interface ValidPromoCode {

	String message() default "{acme.validation.promoCode.message}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
