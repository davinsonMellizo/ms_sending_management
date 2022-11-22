package co.com.bancolombia.api.handlers;

import co.com.bancolombia.commons.exception.TechnicalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.TECHNICAL_MISSING_PARAMETERS;

@Component
@RequiredArgsConstructor
public class ValidatorHandler {

    private final Validator validator;

    public <T> void validateObject(T object) {
        Set<ConstraintViolation<T>> constraints = validator.validate(object);
        if (!constraints.isEmpty()) {
            throw new TechnicalException(getMessage(constraints), TECHNICAL_MISSING_PARAMETERS);
        }
    }

    private <T> String getMessage(Set<ConstraintViolation<T>> constraintViolations) {
        return constraintViolations.stream()
                .map(c -> String.join(" ", c.getPropertyPath().toString(), c.getMessage()))
                .collect(Collectors.joining(", "));
    }

}
