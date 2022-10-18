package co.com.bancolombia.api.schedule;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.services.schedule.ScheduleHandler;
import co.com.bancolombia.api.services.schedule.ScheduleRouter;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.model.schedule.Schedule;
import co.com.bancolombia.usecase.schedule.ScheduleUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.SCHEDULE_NOT_FOUND;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.INTERNAL_SERVER_ERROR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        ScheduleRouter.class,
        ScheduleHandler.class,
        ApiProperties.class,
        ValidatorHandler.class,
        ExceptionHandler.class
})
class ScheduleRouterWithExceptionTest extends BaseIntegration {
    @MockBean
    private ScheduleUseCase useCase;

    @MockBean
    private LoggerBuilder loggerBuilder;

    private String request;

    private final Schedule schedule = new Schedule();
    private String url;
    private final static String ID = "/{id}";

    @BeforeEach
    void init() {
        url = properties.getSchedule();
        request = loadFileConfig("ScheduleRequest.json", String.class);
    }

    @Test
    void saveScheduleWithException() {
        when(useCase.saveSchedule(any())).thenReturn(Mono.error(new TechnicalException(INTERNAL_SERVER_ERROR)));
        statusAssertionsWebClientPost(url, request)
                .is5xxServerError();
        verify(useCase).saveSchedule(any());
    }

    @Test
    void findScheduleByIdWithException() {
        when(useCase.findScheduleById(any())).thenReturn(Mono.error(new BusinessException(SCHEDULE_NOT_FOUND)));
        final WebTestClient.ResponseSpec spec = webTestClient.get().uri(url + ID, "1").exchange();
        spec.expectStatus().is5xxServerError();
        verify(useCase).findScheduleById(any());
    }

    @Test
    void updateCampaignWithException() {
        when(useCase.updateSchedule(any(), anyLong())).thenReturn(Mono.error(new BusinessException(SCHEDULE_NOT_FOUND)));
        statusAssertionsWebClientPut(url + "/1", request)
                .is5xxServerError();
        verify(useCase).updateSchedule(any(), anyLong());
    }

}
