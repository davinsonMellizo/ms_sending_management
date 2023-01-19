package co.com.bancolombia.model.commons.exception;


import org.junit.jupiter.api.Test;

import static co.com.bancolombia.model.commons.enums.TechnicalExceptionEnum.TECHNICAL_MISSING_PARAMETERS;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TechnicalExceptionTest {

    @Test
    void exceptionWithEnum() {
        TechnicalException exception = new TechnicalException(TECHNICAL_MISSING_PARAMETERS);
        assertEquals(exception.getException().getMessage(), exception.getException().getMessage());
    }

    @Test
    void exceptionWithEnumAndMessage() {
        TechnicalException exception = new TechnicalException("Exception", TECHNICAL_MISSING_PARAMETERS);
        assertEquals(exception.getException().getMessage(), exception.getException().getMessage());
    }

}
