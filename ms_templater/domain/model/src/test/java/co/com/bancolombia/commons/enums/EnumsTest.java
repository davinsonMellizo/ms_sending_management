package co.com.bancolombia.commons.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EnumsTest {

    @Test
    void technicalExceptionEnumGetType() {
        assertThat(TechnicalExceptionEnum.TECHNICAL_SAVE.getMessage())
                .isInstanceOf(String.class);
        assertThat(TechnicalExceptionEnum.TECHNICAL_SAVE.getCode())
                .isEqualTo(TechnicalExceptionEnum.TECHNICAL_SAVE.getCode());
    }

}
