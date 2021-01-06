package DataValidation;

import Model.Notification;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TargetValidator implements ConstraintValidator<TargetConstraint, Notification> {
    @Override
    public void initialize(TargetConstraint constraintAnnotation) {}

    @Override
    public boolean isValid(Notification notification, ConstraintValidatorContext constraintValidatorContext) {
        if(notification.getType().equals("SMS"))
            return notification.getTarget().matches("^\\+?[0-9]{7,15}$");
        if(notification.getType().equals("MAIL"))
            return notification.getTarget().matches("^[A-Za-z0-9+_.-]+@(.+)$");
        return false;
    }
}
