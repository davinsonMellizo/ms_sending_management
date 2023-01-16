package co.com.bancolombia.usecase.schedule;

import co.com.bancolombia.commons.enums.ScheduleType;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.campaign.gateways.CampaignGlueGateway;
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
import java.util.List;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.SCHEDULE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Mock
    private CampaignGlueGateway campaignGlueGateway;

    private final Campaign campaign = new Campaign();
    private final Schedule schedule = new Schedule();
    private static final LocalDate DATE_NOW = LocalDate.now();
    private static final LocalTime TIME_NOW = LocalTime.now();

    @BeforeEach
    public void init() {
        campaign.setIdCampaign("1");
        campaign.setIdConsumer("ALM");
        schedule.setId(1L);
        schedule.setIdCampaign(campaign.getIdCampaign());
        schedule.setIdConsumer(campaign.getIdConsumer());
        schedule.setScheduleType(ScheduleType.DAILY);
        schedule.setStartDate(DATE_NOW);
        schedule.setStartTime(TIME_NOW);
        schedule.setEndDate(DATE_NOW.plusMonths(1));
        schedule.setEndTime(TIME_NOW);
        campaign.setSchedules(List.of(schedule));
    }

    @Test
    void saveSchedule() {
        when(scheduleGateway.saveSchedule(any()))
                .thenReturn(Mono.just(campaign));

        when(campaignGlueGateway.createTrigger(any()))
                .thenReturn(Mono.just(campaign));

        StepVerifier.create(useCase.saveSchedule(schedule))
                .assertNext(response -> assertEquals(response.getId(), schedule.getId()))
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
                .thenReturn(Mono.just(StatusResponse.<Campaign>builder()
                        .actual(campaign).before(campaign).build()));

        when(campaignGlueGateway.updateTrigger(any()))
                .thenReturn(Mono.just(StatusResponse.<Campaign>builder()
                        .actual(campaign).before(campaign).build()));

        StepVerifier.create(useCase.updateSchedule(schedule, schedule.getId()))
                .assertNext(response -> assertEquals(response.getActual().getId(), schedule.getId()))
                .verifyComplete();

        verify(scheduleGateway).updateSchedule(any(), anyLong());
    }

    @Test
    void updateScheduleWithException() {
        when(scheduleGateway.updateSchedule(any(), anyLong()))
                .thenReturn(Mono.error(new BusinessException(SCHEDULE_NOT_FOUND)));

        useCase.updateSchedule(schedule, schedule.getId())
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }

}
