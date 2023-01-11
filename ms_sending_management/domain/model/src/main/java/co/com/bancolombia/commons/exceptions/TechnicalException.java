package co.com.bancolombia.commons.exceptions;


import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import lombok.Getter;

@Getter
public class TechnicalException extends RuntimeException {

    private final TechnicalExceptionEnum exception;

    public TechnicalException(Throwable error, TechnicalExceptionEnum technicalExceptionEnum) {
        super(error);
        this.exception = technicalExceptionEnum;
    }

    public TechnicalException(TechnicalExceptionEnum technicalExceptionEnum) {
        super(technicalExceptionEnum.getMessage());
        this.exception = technicalExceptionEnum;
    }
}