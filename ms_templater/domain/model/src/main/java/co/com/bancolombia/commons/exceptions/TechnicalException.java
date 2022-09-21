package co.com.bancolombia.commons.exceptions;

import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TechnicalException extends RuntimeException {

    private final TechnicalExceptionEnum exception;

    public TechnicalException(String message, TechnicalExceptionEnum technicalExceptionEnum) {
        super(message);
        this.exception = technicalExceptionEnum;
    }
}
