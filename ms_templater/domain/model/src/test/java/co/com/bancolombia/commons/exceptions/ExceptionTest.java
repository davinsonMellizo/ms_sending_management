package co.com.bancolombia.commons.exceptions;

import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExceptionTest {

    @Test
    void technicalExceptionStructureTest() {
        TechnicalExceptionEnum exceptionEnum = TechnicalExceptionEnum.TECHNICAL_SAVE;
        TechnicalException technicalException = new TechnicalException("Error", TechnicalExceptionEnum.TECHNICAL_SAVE);
        assertThat(technicalException.getMessage()).isEqualTo("Error");
        assertThat(technicalException.getException().getMessage()).isEqualTo(exceptionEnum.getMessage());
        assertThat(technicalException.getException().getCode()).isEqualTo(exceptionEnum.getCode());
    }
}
