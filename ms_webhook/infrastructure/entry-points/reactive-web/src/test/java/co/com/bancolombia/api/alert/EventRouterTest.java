package co.com.bancolombia.api.alert;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.services.event.EventHandler;
import co.com.bancolombia.api.services.event.EventRouter;
import co.com.bancolombia.log.LoggerBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        EventHandler.class,
        EventRouter.class,
        ApiProperties.class
})
public class EventRouterTest extends BaseIntegration {

    @MockBean
    private LoggerBuilder loggerBuilder;


    @Test
    void eventRegister() {
        statusAssertionsWebClientPost(properties.getEventRegister(), "{}")
                .isOk()
                .expectBody(String.class)
                .returnResult();
    }


}
