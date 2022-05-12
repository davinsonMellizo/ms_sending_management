package co.com.bancolombia.api.campaign;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.services.campaign.CampaignHandler;
import co.com.bancolombia.api.services.campaign.CampaignRouter;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
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

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CATEGORY_NOT_FOUND;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.INTERNAL_SERVER_ERROR;
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
public class CampaignRouterTest extends BaseIntegration {

    @MockBean
    private CampaignUseCase useCase;

    @MockBean
    private LoggerBuilder loggerBuilder;

    private String request;
    private final Campaign campaign = new Campaign();
    private String url;
    private final static String ALL = "/all-campaign";

    @BeforeEach
    public void init() {
        url = properties.getCampaign();
        request = loadFileConfig("CampaignRequest.json", String.class);
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
    void findAll() {
        when(useCase.findAllCampaign())
                .thenReturn(Mono.just(List.of(campaign)));
        final WebTestClient.ResponseSpec spec = webTestClient.get().uri(url + ALL)
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).findAllCampaign();
    }

    @Test
    void update() {
        when(useCase.updateCampaign(any())).thenReturn(Mono.just(StatusResponse.<Campaign>builder()
                .actual(campaign).before(campaign).build()));
        statusAssertionsWebClientPut(url,
                request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).updateCampaign(any());
    }

    @Test
    void delete() {
        when(useCase.deleteCampaignById(any()))
                .thenReturn(Mono.just("1"));
        WebTestClient.ResponseSpec spec = webTestClient.delete().uri(url)
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).deleteCampaignById(any());
    }

    @Test
    void saveCampaignWithException() {
        when(useCase.saveCampaign(any())).thenReturn(Mono.error(new TechnicalException(INTERNAL_SERVER_ERROR)));
        statusAssertionsWebClientPost(url, request)
                .is5xxServerError();
        verify(useCase).saveCampaign(any());
    }

    @Test
    void updateCampaignWithException() {
        when(useCase.updateCampaign(any())).thenReturn(Mono.error(new BusinessException(CATEGORY_NOT_FOUND)));
        statusAssertionsWebClientPut(url, request)
                .is5xxServerError();
    }
}
