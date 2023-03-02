package co.com.bancolombia.commons.exceptions;


import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import lombok.Getter;

@Getter
public class TechnicalException extends RuntimeException {

    private  TechnicalExceptionEnum exception;
    private  Integer code;

    public TechnicalException(Throwable error, TechnicalExceptionEnum technicalExceptionEnum) {
        super(error);
        this.exception = technicalExceptionEnum;
    }
    public TechnicalException(String message, TechnicalExceptionEnum technicalExceptionEnum) {
        super(message);
        this.exception = technicalExceptionEnum;
    }
    public TechnicalException(String message,  Integer code) {
        super(message);
        this.code = code;
    }
    public TechnicalException( String message, TechnicalExceptionEnum technicalExceptionEnum, Integer code) {
        super(message);
        this.exception = technicalExceptionEnum;
        this.code = code;
    }

    public TechnicalException(TechnicalExceptionEnum technicalExceptionEnum) {
        super(technicalExceptionEnum.getMessage());
        this.exception = technicalExceptionEnum;
    }
}