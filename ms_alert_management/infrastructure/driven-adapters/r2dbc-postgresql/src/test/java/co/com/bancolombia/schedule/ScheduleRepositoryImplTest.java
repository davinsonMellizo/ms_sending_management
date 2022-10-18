package co.com.bancolombia.schedule;

import co.com.bancolombia.commons.enums.ScheduleType;
import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.schedule.Schedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ScheduleRepositoryImplTest {

    @Autowired
    private ScheduleRepositoryImplement repositoryImpl;

    private final Campaign campaign = new Campaign();
    private final Schedule schedule = new Schedule();
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
        campaign.setState("1");
        campaign.setCreatedDate(NOW);
        campaign.setCreationUser("lugomez");

        schedule.setId(1L);
        schedule.setIdCampaign("1");
        schedule.setIdConsumer("ALM");
        schedule.setScheduleType(ScheduleType.DAILY);
        schedule.setStartDate(DATE_NOW);
        schedule.setStartTime(TIME_NOW);
        schedule.setEndDate(DATE_NOW.plusMonths(1));
        schedule.setEndTime(TIME_NOW);

        campaign.setSchedules(List.of(schedule));
    }

    @Test
    void saveSchedule() {
        schedule.setId(4L);
        repositoryImpl.saveSchedule(schedule)
                .subscribe(s -> StepVerifier
                        .create(repositoryImpl.findScheduleById(s.getId()))
                        .expectNextCount(1)
                        .consumeNextWith(status -> assertEquals(status.getId(), schedule.getId()))
                        .verifyComplete()
                );
    }

    @Test
    void updateSchedule() {
        schedule.setScheduleType(ScheduleType.HOURLY);
        StepVerifier.create(repositoryImpl.updateSchedule(schedule, schedule.getId()))
                .consumeNextWith(status ->
                        assertEquals(schedule.getScheduleType(), status.getActual().getScheduleType()))
                .verifyComplete();
    }

    @Test
    void findScheduleById() {
        StepVerifier.create(repositoryImpl.findScheduleById(schedule.getId()))
                .consumeNextWith(scheduleFound -> assertEquals(schedule.getId(), scheduleFound.getId()))
                .verifyComplete();
    }

    @Test
    void saveSchedulesByCampaign() {
        repositoryImpl.saveSchedulesByCampaign(campaign)
                .subscribe(alertSaved -> StepVerifier
                        .create(repositoryImpl.findSchedulesByCampaign(campaign))
                        .expectNextCount(1)
                        .consumeNextWith(status -> assertEquals(campaign.getSchedules().get(0).getId(), status.getSchedules().get(0).getId()))
                        .verifyComplete());
    }

    @Test
    void findSchedulesByCampaign() {
        StepVerifier.create(repositoryImpl.findSchedulesByCampaign(campaign))
                .consumeNextWith(allSchedules -> assertEquals(2, allSchedules.getSchedules().size()))
                .verifyComplete();
    }

    @Test
    void updateSchedulesByCampaign() {
        campaign.getSchedules().get(0).setScheduleType(ScheduleType.HOURLY);
        StepVerifier.create(repositoryImpl.updateSchedulesByCampaign(campaign))
                .consumeNextWith(status -> assertEquals(campaign.getSchedules().get(0).getScheduleType(), status.getSchedules().get(0).getScheduleType()))
                .verifyComplete();
    }
}
