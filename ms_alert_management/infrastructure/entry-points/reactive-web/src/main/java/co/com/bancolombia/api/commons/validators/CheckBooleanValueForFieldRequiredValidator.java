package co.com.bancolombia.api.commons.validators;

import co.com.bancolombia.api.commons.validators.constraints.CheckBooleanValueForFieldRequired;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckBooleanValueForFieldRequiredValidator implements ConstraintValidator<CheckBooleanValueForFieldRequired, Object> {
    private String checkedField;
    private boolean checkedValue;
    private String requiredField;

    @Override
    public void initialize(CheckBooleanValueForFieldRequired constraintAnnotation) {
        this.checkedField = constraintAnnotation.checkedField();
        this.checkedValue = constraintAnnotation.checkedValue();
        this.requiredField = constraintAnnotation.requiredField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object checkedFieldValue = new BeanWrapperImpl(value)
                .getPropertyValue(checkedField);
        Object requiredFieldValue = new BeanWrapperImpl(value)
                .getPropertyValue(requiredField);

        return checkedFieldValue != null && !checkedFieldValue.equals(checkedValue) || requiredFieldValue != null;
    }
}
