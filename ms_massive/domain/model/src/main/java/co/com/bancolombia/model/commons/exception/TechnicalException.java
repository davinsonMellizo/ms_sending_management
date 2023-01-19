package co.com.bancolombia.model.commons.exception;

import co.com.bancolombia.model.commons.enums.TechnicalExceptionEnum;
import lombok.Getter;

@Getter
public class TechnicalException extends RuntimeException {

    private final TechnicalExceptionEnum exception;

    public TechnicalException(TechnicalExceptionEnum technicalExceptionEnum) {
        super(technicalExceptionEnum.getMessage());
        this.exception = technicalExceptionEnum;
    }

    public TechnicalException(String message, TechnicalExceptionEnum technicalExceptionEnum) {
        super(message);
        this.exception = technicalExceptionEnum;
    }

}
