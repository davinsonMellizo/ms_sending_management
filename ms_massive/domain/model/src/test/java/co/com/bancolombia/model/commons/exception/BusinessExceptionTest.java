package co.com.bancolombia.model.commons.exception;

import org.junit.jupiter.api.Test;

import static co.com.bancolombia.model.commons.enums.BusinessExceptionEnum.BUSINESS_CAMPAIGN_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BusinessExceptionTest {

    @Test
    void exceptionWithEnum() {
        BusinessException exception = new BusinessException(BUSINESS_CAMPAIGN_NOT_FOUND);
        assertEquals(exception.getException().getMessage(), exception.getMessage());
    }

}
