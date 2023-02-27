package co.com.bancolombia.api.commons.validators;

import co.com.bancolombia.api.commons.validators.constraints.ProviderFormat;
import com.fasterxml.jackson.databind.JsonNode;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ProviderFormatValidator implements ConstraintValidator<ProviderFormat, JsonNode> {

    @Override
    public void initialize(ProviderFormat providerFormat) {
        ConstraintValidator.super.initialize(providerFormat);
    }

    @Override
    public boolean isValid(JsonNode value, ConstraintValidatorContext context) {
        if(value == null)  return false;
        for (int i = 0; i < value.size(); i++) {
            if (!value.get(i).has("idProvider") || !value.get(i).has("channelType")) return false;
        }
        return true;
    }
}