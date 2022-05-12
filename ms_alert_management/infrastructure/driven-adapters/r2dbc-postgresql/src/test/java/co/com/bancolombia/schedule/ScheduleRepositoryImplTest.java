package co.com.bancolombia.schedule;

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
public class ScheduleRepositoryImplTest {

    @Autowired
    private ScheduleRepositoryImplement repositoryImpl;

    private final Campaign campaign = new Campaign();
    private final Schedule schedule = new Schedule();
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDate DATE_NOW = LocalDate.now();
    private static final LocalTime TIME_NOW = LocalTime.now();

    @BeforeEach
    public void init() {
        campaign.setId(1);
        campaign.setIdCampaign("1");
        campaign.setIdConsumer("ALM");
        campaign.setIdProvider("HJK");
        campaign.setIdRemitter(0);
        campaign.setDefaultTemplate("template");
        campaign.setDescription("description");
        campaign.setSourcePath("source_path");
        campaign.setAttachment(true);
        campaign.setAttachmentPath("attachment_path");
        campaign.setState("ACTIVO");
        campaign.setCreatedDate(NOW);
        campaign.setCreationUser("lugomez");

        schedule.setId(1);
        schedule.setIdCampaign("1");
        schedule.setIdConsumer("0");
        schedule.setScheduleType("MENSUAL");
        schedule.setStartDate(DATE_NOW);
        schedule.setStartTime(TIME_NOW);
        schedule.setEndDate(DATE_NOW.plusMonths(1));
        schedule.setEndTime(TIME_NOW);

        campaign.setSchedules(List.of(schedule));
    }

    @Test
    void findSchedulesByCampaign() {
        StepVerifier.create(repositoryImpl.findSchedulesByCampaign(campaign))
                .consumeNextWith(allSchedules -> assertEquals(2, allSchedules.getSchedules().size()))
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
    void updateSchedulesByCampaign() {
        campaign.getSchedules().get(0).setScheduleType("SEMANAL");
        StepVerifier.create(repositoryImpl.updateSchedulesByCampaign(campaign))
                .consumeNextWith(status -> assertEquals(campaign.getSchedules().get(0).getScheduleType(), status.getSchedules().get(0).getScheduleType()))
                .verifyComplete();
    }

    @Test
    void deleteSchedulesByCampaign() {
        campaign.setIdCampaign("2");
        campaign.setIdConsumer("1");
        StepVerifier.create(repositoryImpl.deleteSchedulesByCampaign(campaign))
                .consumeNextWith(s -> assertEquals(campaign.getIdCampaign(), s))
                .verifyComplete();
    }
}
