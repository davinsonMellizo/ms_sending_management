import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TechnicalExceptionEnumTest {


    @Test
    public void buildExceptionThrowableFromTypeTechnical(){
        TechnicalExceptionEnum exception = TechnicalExceptionEnum.TECHNICAL_RESTCLIENT_ERROR;
        assertThat(exception.build(new Throwable())).isInstanceOf(TechnicalException.class);
    }

}
