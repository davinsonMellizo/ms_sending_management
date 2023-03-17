
import co.com.bancolombia.commons.exceptions.BusinessException;
import org.junit.jupiter.api.Test;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.REQUIRED_MESSAGE_TEMPLATE;
import static org.assertj.core.api.Assertions.assertThat;

class BusinessExceptionTest {

    BusinessException businessException= new BusinessException(REQUIRED_MESSAGE_TEMPLATE);

    @Test
    void BusinessETest(){
        assertThat(businessException).isNotNull();
    }

}
