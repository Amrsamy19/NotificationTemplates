package DataValidation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TargetValidator.class)
public @interface TargetConstraint {
    String message() default "Type and Target doesn't match";
    Class<?>[] groups() default {};
    public abstract Class<? extends Payload>[] payload() default {};
}
