package co.com.bancolombia.api.commons.handlers;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.log.LoggerBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.HEADER_MISSING_ERROR;

@Component
@RequiredArgsConstructor
public class ValidatorHandler {

    private final Validator validator;
    private final LoggerBuilder logger;

    public <T> void validateObject(T object) {
        Set<ConstraintViolation<T>> constraints = validator.validate(object);
        if (!constraints.isEmpty()) {
            logger.error(new TechnicalException(getMessage(constraints), BODY_MISSING_ERROR));
            throw new TechnicalException(getMessage(constraints), BODY_MISSING_ERROR);
        }
    }

    public <T> void validateObjectHeaders(T object) {
        Set<ConstraintViolation<T>> constraints = validator.validate(object);
        if (!constraints.isEmpty()) {
            throw new TechnicalException(getMessage(constraints), HEADER_MISSING_ERROR);
        }
    }

    private <T> String getMessage(Set<ConstraintViolation<T>> constraintViolations) {
        return constraintViolations.stream()
                .map(c -> String.join(" ", c.getPropertyPath().toString(), c.getMessage()))
                .collect(Collectors.joining(", "));
    }

}