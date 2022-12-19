package co.com.bancolombia.model.commons.exception;

import co.com.bancolombia.model.commons.enums.BusinessExceptionEnum;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final BusinessExceptionEnum exception;

    public BusinessException(BusinessExceptionEnum businessExceptionEnum) {
        super(businessExceptionEnum.getMessage());
        this.exception = businessExceptionEnum;
    }
}
