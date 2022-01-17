package co.com.bancolombia.api.commons;

import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RequestValidator {

    private final Validator validator;

    public <T> void validateBody(T object) {
        Set<ConstraintViolation<T>> constraint = validator.validate(object);
        if (!constraint.isEmpty()) {
            throw new TechnicalException(getMessage(constraint), TechnicalExceptionEnum.TECHNICAL_PARAMETER);
        }
    }

    private <T> String getMessage(Set<ConstraintViolation<T>> constraintViolations) {
        return constraintViolations.stream()
                .map(c -> String.join(": ", c.getPropertyPath().toString(), c.getMessage()))
                .collect(Collectors.joining(", "));
    }
}
