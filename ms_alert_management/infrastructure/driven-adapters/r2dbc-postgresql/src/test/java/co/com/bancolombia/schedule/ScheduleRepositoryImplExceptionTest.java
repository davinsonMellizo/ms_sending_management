package co.com.bancolombia.schedule;

import co.com.bancolombia.commons.enums.ScheduleType;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.schedule.Schedule;
import co.com.bancolombia.schedule.data.ScheduleData;
import co.com.bancolombia.schedule.data.ScheduleMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SpringBootTest
class ScheduleRepositoryImplExceptionTest {

    @InjectMocks
    private ScheduleRepositoryImplement repositoryImpl;

    @Mock
    private ScheduleRepository repository;

    @Spy
    private ScheduleMapper mapper = Mappers.getMapper(ScheduleMapper.class);

    @Mock
    private TimeFactory timeFactory;

    private final Campaign campaign = new Campaign();
    private final Schedule schedule = new Schedule();
    private final ScheduleData scheduleData = new ScheduleData();
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDate DATE_NOW = LocalDate.now();
    private static final LocalTime TIME_NOW = LocalTime.now();

    @BeforeEach
    public void init() {
        campaign.setIdCampaign("1");
        campaign.setIdConsumer("ALM");
        campaign.setProvider("{\"idProvider\":\"INA\",\"channelType\":\"PUSH\"}");
        campaign.setIdRemitter(0);
        campaign.setSourcePath("source_path");
        campaign.setAttachment(false);
        campaign.setState("0");
        campaign.setCreatedDate(NOW);
        campaign.setCreationUser("lugomez");

        schedule.setId(1L);
        schedule.setIdCampaign("1");
        schedule.setIdConsumer("ALM");
        schedule.setScheduleType(ScheduleType.ON_DEMAND);
        schedule.setStartDate(DATE_NOW);
        schedule.setStartTime(TIME_NOW);
        schedule.setEndDate(DATE_NOW.plusMonths(1));
        schedule.setEndTime(TIME_NOW);

        scheduleData.setId(2L);
        scheduleData.setIdCampaign("1");
        scheduleData.setIdConsumer("ALM");
        scheduleData.setScheduleType(ScheduleType.DAILY);
        scheduleData.setStartDate(DATE_NOW);
        scheduleData.setStartTime(TIME_NOW);

        campaign.setSchedules(List.of(schedule));
    }

    @Test
    void saveScheduleWithException() {
        when(repository.save(any()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.saveSchedule(schedule)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void updateScheduleWithException() {
        when(repository.save(any()))
                .thenReturn(Mono.error(RuntimeException::new));
        when(repository.findById(anyLong()))
                .thenReturn(Mono.just(scheduleData));
        repositoryImpl.updateSchedule(schedule, schedule.getId())
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void findScheduleByIdWithException() {
        when(repository.findById(anyLong()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.findScheduleById(schedule.getId())
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void findSchedulesByCampaignWithException() {
        when(repository.findSchedulesByCampaign(anyString(), anyString()))
                .thenReturn(Flux.error(RuntimeException::new));
        repositoryImpl.findSchedulesByCampaign(campaign)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void saveSchedulesByCampaignWithException() {
        when(repository.save(any()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.saveSchedulesByCampaign(campaign)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void updateSchedulesByCampaignWithException() {
        when(repository.save(any()))
                .thenReturn(Mono.error(RuntimeException::new));
        when(repository.findSchedulesByCampaign(anyString(), anyString()))
                .thenReturn(Flux.just(scheduleData));
        repositoryImpl.updateSchedulesByCampaign(campaign)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }
}
