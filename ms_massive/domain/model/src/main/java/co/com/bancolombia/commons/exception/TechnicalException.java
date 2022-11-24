package co.com.bancolombia.commons.exception;

import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import lombok.Getter;

@Getter
public class TechnicalException extends RuntimeException {

    private final TechnicalExceptionEnum exception;

    public TechnicalException(String message, TechnicalExceptionEnum technicalExceptionEnum) {
        super(message);
        this.exception = technicalExceptionEnum;
    }

    public TechnicalException(Throwable cause, TechnicalExceptionEnum technicalExceptionEnum) {
        super(cause);
        this.exception = technicalExceptionEnum;
    }

    public TechnicalException(TechnicalExceptionEnum technicalExceptionEnum) {
        super(technicalExceptionEnum.getMessage());
        this.exception = technicalExceptionEnum;
    }

}

