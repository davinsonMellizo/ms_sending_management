package co.com.bancolombia.api.campaign;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.handlers.ExceptionHandler;
import co.com.bancolombia.api.handlers.ValidatorHandler;
import co.com.bancolombia.api.service.Handler;
import co.com.bancolombia.api.service.Router;
import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.usecase.MassiveUseCase;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        Router.class,
        Handler.class,
        ApiProperties.class,
        ValidatorHandler.class,
        ExceptionHandler.class
})
class CampaignRouterTest extends BaseIntegration {

    @MockBean
    private MassiveUseCase useCase;
    private String request;
    private final Campaign campaign = new Campaign();
    private String url;

    @BeforeEach
    void init() {
        url = properties.getSendCampaign();
        request = loadFileConfig("CampaignRequest.json", String.class);
        campaign.setIdCampaign("15");
        campaign.setIdConsumer("SVP");
    }

    @Test
    void campaignSuccess() {
        when(useCase.sendCampaign(any())).thenReturn(Mono.just(StatusResponse.builder().build()));
        statusAssertionsWebClientPost(url, request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).sendCampaign(any());
    }

}
