package co.com.bancolombia.api.campaign;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.handlers.ExceptionHandler;
import co.com.bancolombia.api.handlers.ValidatorHandler;
import co.com.bancolombia.api.service.Handler;
import co.com.bancolombia.api.service.Router;
import co.com.bancolombia.model.commons.exception.BusinessException;
import co.com.bancolombia.model.commons.exception.TechnicalException;
import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.usecase.MassiveUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.model.commons.enums.BusinessExceptionEnum.*;
import static co.com.bancolombia.model.commons.enums.TechnicalExceptionEnum.TECHNICAL_MISSING_PARAMETERS;
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
class CampaignRouterWithExceptionTest extends BaseIntegration {
    @MockBean
    private MassiveUseCase useCase;
    private String request;
    private String url;


    @BeforeEach
    void init() {
        url = properties.getSendCampaign();
        request = loadFileConfig("CampaignRequest.json", String.class);
    }


    @Test
    void startCampaignWithBusinessException() {
        when(useCase.sendCampaign(any()))
                .thenReturn(Mono.error(new BusinessException(BUSINESS_CAMPAIGN_NOT_FOUND)));
        statusAssertionsWebClientPost(url, request)
                .is5xxServerError();
        verify(useCase).sendCampaign(any());
    }

    @Test
    void startCampaignWithoutOnDemanSchedule() {
        when(useCase.sendCampaign(any()))
                .thenReturn(Mono.error(new BusinessException(BUSINESS_CAMPAIGN_WITHOUT_SCHEDULE_ON_DEMAND)));
        statusAssertionsWebClientPost(url, request)
                .is5xxServerError();
        verify(useCase).sendCampaign(any());
    }

    @Test
    void startCampaignInActive() {
        when(useCase.sendCampaign(any()))
                .thenReturn(Mono.error(new BusinessException(BUSINESS_CAMPAIGN_IS_INACTIVE)));
        statusAssertionsWebClientPost(url, request)
                .is5xxServerError();
        verify(useCase).sendCampaign(any());
    }

    @Test
    void startCampaignWithTechnicalException() {
        when(useCase.sendCampaign(any()))
                .thenReturn(Mono.error(new TechnicalException(TECHNICAL_MISSING_PARAMETERS)));
        statusAssertionsWebClientPost(url, request)
                .is5xxServerError();
        verify(useCase).sendCampaign(any());
    }

    @Test
    void startCampaignWithThrowableException() {
        when(useCase.sendCampaign(any()))
                .thenReturn(Mono.error(new RuntimeException("Error")));
        statusAssertionsWebClientPost(url, request)
                .is5xxServerError();
        verify(useCase).sendCampaign(any());
    }

}
