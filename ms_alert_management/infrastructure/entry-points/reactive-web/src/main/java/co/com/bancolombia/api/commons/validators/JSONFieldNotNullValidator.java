package co.com.bancolombia.api.commons.validators;

import co.com.bancolombia.api.commons.validators.constraints.JSONFieldNotNull;
import com.fasterxml.jackson.databind.JsonNode;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class JSONFieldNotNullValidator implements ConstraintValidator<JSONFieldNotNull, JsonNode> {

    @Override
    public void initialize(JSONFieldNotNull constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(JsonNode value, ConstraintValidatorContext context) {
        if (value != null) {
            return !value.isNull() && !value.isEmpty();
        }
        return false;
    }
}
