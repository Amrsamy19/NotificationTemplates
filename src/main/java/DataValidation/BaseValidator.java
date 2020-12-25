package DataValidation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;

public class BaseValidator<T> implements IValidator<T> {
    protected final Validator validatorUnit;

    public BaseValidator(Validator validatorUnit) {
        this.validatorUnit = validatorUnit;
    }

    public BaseValidator(){
        this.validatorUnit = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public Set<ConstraintViolation<T>> validate(T entity) {
        return validatorUnit.validate(entity);
    }

    public String isValid(Set<ConstraintViolation<T>> constraintViolations) {
        if(!constraintViolations.isEmpty()) {
            StringBuilder violationMessage = new StringBuilder();
            for(ConstraintViolation<T> violation : constraintViolations)
                violationMessage.append("{\"")
                        .append(violation.getPropertyPath())
                        .append("\":\"")
                        .append(violation.getMessageTemplate())
                        .append("\"}");

            return violationMessage.toString();
        }
        return null;
    }
}
