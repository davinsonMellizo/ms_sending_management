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
import co.com.bancolombia.usecase.campaign.CampaignUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CAMPAIGN_NOT_FOUND;
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
class CampaignRouterWithExceptionTest extends BaseIntegration {
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
    }

    @Test
    void saveCampaignWithException() {
        when(useCase.saveCampaign(any())).thenReturn(Mono.error(new TechnicalException(INTERNAL_SERVER_ERROR)));
        statusAssertionsWebClientPost(url, request)
                .is5xxServerError();
        verify(useCase).saveCampaign(any());
    }

    @Test
    void findCampaignByIdWithException() {
        when(useCase.findCampaignById(any())).thenReturn(Mono.error(new BusinessException(CAMPAIGN_NOT_FOUND)));
        final WebTestClient.ResponseSpec spec = webTestClient.get().uri(url).exchange();
        spec.expectStatus().is5xxServerError();
        verify(useCase).findCampaignById(any());
    }

    @Test
    void updateCampaignWithException() {
        when(useCase.updateCampaign(any())).thenReturn(Mono.error(new BusinessException(CAMPAIGN_NOT_FOUND)));
        statusAssertionsWebClientPut(url, request)
                .is5xxServerError();
        verify(useCase).updateCampaign(any());
    }

    @Test
    void deleteCampaignWithException() {
        when(useCase.deleteCampaignById(any())).thenReturn(Mono.error(new BusinessException(CAMPAIGN_NOT_FOUND)));
        statusAssertionsWebClientDelete(url, request)
                .is5xxServerError();
        verify(useCase).deleteCampaignById(any());
    }
}
