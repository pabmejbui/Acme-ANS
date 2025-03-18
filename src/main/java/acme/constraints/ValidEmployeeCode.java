
package acme.constraints;

public @interface ValidEmployeeCode {

	String message() default "The first two or three letters of the id correspond to their initials";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
