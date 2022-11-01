package co.com.bancolombia.api.schedule;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.services.schedule.ScheduleHandler;
import co.com.bancolombia.api.services.schedule.ScheduleRouter;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.model.schedule.Schedule;
import co.com.bancolombia.usecase.schedule.ScheduleUseCase;
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
class ScheduleRouterTest extends BaseIntegration {
    @MockBean
    private ScheduleUseCase useCase;

    @MockBean
    private LoggerBuilder loggerBuilder;

    private String request;

    private final Schedule schedule = new Schedule();
    private String url;
    private final static String ID = "/{id}";

    @BeforeEach
    public void init() {
        url = properties.getSchedule();
        request = loadFileConfig("ScheduleRequest.json", String.class);
    }

    @Test
    void saveSchedule() {
        when(useCase.saveSchedule(any())).thenReturn(Mono.just(schedule));
        statusAssertionsWebClientPost(url, request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).saveSchedule(any());
    }

    @Test
    void findScheduleById() {
        when(useCase.findScheduleById(any()))
                .thenReturn(Mono.just(schedule));
        final WebTestClient.ResponseSpec spec = webTestClient.get()
                .uri(url + ID, "1").exchange();
        spec.expectStatus().isOk();
        verify(useCase).findScheduleById(any());
    }

    @Test
    void updateSchedule() {
        when(useCase.updateSchedule(any(), anyLong())).thenReturn(Mono.just(StatusResponse.<Schedule>builder()
                .actual(schedule).before(schedule).build()));
        statusAssertionsWebClientPut(url + "/1", request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).updateSchedule(any(), anyLong());
    }

}
