package co.com.bancolombia.usecase.schedule;

import co.com.bancolombia.commons.enums.ScheduleType;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.model.schedule.Schedule;
import co.com.bancolombia.model.schedule.gateways.ScheduleGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleUseCaseTest {

    @InjectMocks
    private ScheduleUseCase useCase;

    @Mock
    private ScheduleGateway scheduleGateway;

    private final Schedule schedule = new Schedule();
    private static final LocalDate DATE_NOW = LocalDate.now();
    private static final LocalTime TIME_NOW = LocalTime.now();

    @BeforeEach
    public void init() {
        schedule.setId(1L);
        schedule.setIdCampaign("1");
        schedule.setIdConsumer("ALM");
        schedule.setScheduleType(ScheduleType.DAILY);
        schedule.setStartDate(DATE_NOW);
        schedule.setStartTime(TIME_NOW);
        schedule.setEndDate(DATE_NOW.plusMonths(1));
        schedule.setEndTime(TIME_NOW);
    }


    @Test
    void saveSchedule() {
        when(scheduleGateway.saveSchedule(any()))
                .thenReturn(Mono.just(schedule));

        StepVerifier.create(useCase.saveSchedule(schedule))
                .assertNext(response -> response.getId().equals(schedule.getId()))
                .verifyComplete();

        verify(scheduleGateway).saveSchedule(any());
    }

    @Test
    void findScheduleById() {
        when(scheduleGateway.findScheduleById(any()))
                .thenReturn(Mono.just(schedule));

        StepVerifier.create(useCase.findScheduleById(schedule.getId()))
                .expectNextCount(1)
                .verifyComplete();

        verify(scheduleGateway).findScheduleById(any());
    }

    @Test
    void findScheduleByIdWithException() {
        when(scheduleGateway.findScheduleById(anyLong()))
                .thenReturn(Mono.empty());
        useCase.findScheduleById(schedule.getId())
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void updateSchedule() {
        when(scheduleGateway.updateSchedule(any(), anyLong()))
                .thenReturn(Mono.just(StatusResponse.<Schedule>builder()
                        .actual(schedule).before(schedule).build()));

        StepVerifier.create(useCase.updateSchedule(schedule, schedule.getId()))
                .assertNext(response -> response.getActual().getId().equals(schedule.getId()))
                .verifyComplete();

        verify(scheduleGateway).updateSchedule(any(), anyLong());
    }

    @Test
    void updateScheduleWithException() {
        when(scheduleGateway.updateSchedule(any(), anyLong()))
                .thenReturn(Mono.empty());
        useCase.updateSchedule(schedule, schedule.getId())
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }

}