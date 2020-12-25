package DataValidation;

import jakarta.validation.ConstraintViolation;

import java.util.Set;

public interface IValidator<T> {
    Set<ConstraintViolation<T>> validate(T entity);
    String isValid(Set<ConstraintViolation <T>> violations);
}
