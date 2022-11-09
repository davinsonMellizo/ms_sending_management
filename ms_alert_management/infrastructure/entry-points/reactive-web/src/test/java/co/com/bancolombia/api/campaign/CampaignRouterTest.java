package co.com.bancolombia.api.campaign;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.services.campaign.CampaignHandler;
import co.com.bancolombia.api.services.campaign.CampaignRouter;
import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.usecase.campaign.CampaignUseCase;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        CampaignRouter.class,
        CampaignHandler.class,
        ApiProperties.class,
        ValidatorHandler.class,
        ExceptionHandler.class
})
class CampaignRouterTest extends BaseIntegration {

    @MockBean
    private CampaignUseCase useCase;

    @MockBean
    private LoggerBuilder loggerBuilder;

    private String request;
    private final Campaign campaign = new Campaign();
    private String url;
    private final static String ALL = "/all";

    @BeforeEach
    void init() {
        url = properties.getCampaign();
        request = loadFileConfig("CampaignRequest.json", String.class);
        campaign.setIdCampaign("1");
        campaign.setIdConsumer("SVP");
    }

    @Test
    void findAll() {
        when(useCase.findAllCampaign())
                .thenReturn(Mono.just(List.of(campaign)));
        final WebTestClient.ResponseSpec spec = webTestClient.get().uri(url + ALL).exchange();
        spec.expectStatus().isOk();
        verify(useCase).findAllCampaign();
    }

    @Test
    void save() {
        when(useCase.saveCampaign(any()))
                .thenReturn(Mono.just(campaign));
        statusAssertionsWebClientPost(url, request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).saveCampaign(any());
    }

    @Test
    void findCampaignById() {
        when(useCase.findCampaignById(any())).thenReturn(Mono.just(campaign));
        webTestClient.get().uri(uriBuilder -> uriBuilder
                        .path(url)
                        .queryParam("id-campaign", campaign.getIdCampaign())
                        .queryParam("id-consumer", campaign.getIdConsumer())
                        .build())
                .exchange()
                .expectStatus()
                .isOk();
        verify(useCase).findCampaignById(any());
    }

    @Test
    void update() {
        when(useCase.updateCampaign(any())).thenReturn(Mono.just(StatusResponse.<Campaign>builder()
                .actual(campaign).before(campaign).build()));
        statusAssertionsWebClientPut(url, request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).updateCampaign(any());
    }

    @Test
    void delete() {
        when(useCase.deleteCampaignById(any()))
                .thenReturn(Mono.just(
                        Map.of("idCampaign", campaign.getIdCampaign(), "idConsumer", campaign.getIdConsumer())
                ));
        statusAssertionsWebClientDelete(url, request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).deleteCampaignById(any());
    }
}
