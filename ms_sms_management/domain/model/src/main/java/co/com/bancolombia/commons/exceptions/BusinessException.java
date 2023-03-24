package co.com.bancolombia.commons.exceptions;

import co.com.bancolombia.commons.enums.BusinessErrorMessage;
import lombok.Getter;

@Getter
public class BusinessException extends Exception {
    private final BusinessErrorMessage businessErrorMessage;

    public BusinessException(BusinessErrorMessage businessErrorMessage) {
        super(businessErrorMessage.getMessage());
        this.businessErrorMessage = businessErrorMessage;
    }

}