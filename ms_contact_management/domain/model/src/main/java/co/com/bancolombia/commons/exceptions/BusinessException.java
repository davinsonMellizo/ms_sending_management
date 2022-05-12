package co.com.bancolombia.commons.exceptions;

import co.com.bancolombia.commons.enums.BusinessErrorMessage;
import lombok.Getter;

@Getter
public class BusinessException extends Exception {
    private final BusinessErrorMessage exception;

    public BusinessException(BusinessErrorMessage businessExceptionEnum) {
        super(businessExceptionEnum.getMessage());
        this.exception = businessExceptionEnum;
    }
}