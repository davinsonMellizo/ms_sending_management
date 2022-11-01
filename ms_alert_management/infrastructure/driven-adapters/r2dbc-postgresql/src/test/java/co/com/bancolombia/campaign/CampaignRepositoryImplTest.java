package co.com.bancolombia.campaign;

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
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CampaignRepositoryImplTest {

    @Autowired
    private CampaignRepositoryImplement repositoryImpl;

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
        campaign.setDefaultTemplate("template");
        campaign.setDescription("description");
        campaign.setSourcePath("source_path");
        campaign.setAttachment(true);
        campaign.setAttachmentPath("attachment_path");
        campaign.setState("ACTIVO");
        campaign.setCreatedDate(NOW);
        campaign.setCreationUser("lugomez");

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
    void findIdCampaign() {
        StepVerifier.create(repositoryImpl.findCampaignById(campaign))
                .consumeNextWith(campaignFound -> assertEquals(campaign.getIdCampaign(), campaignFound.getIdCampaign()))
                .verifyComplete();
    }

    @Test
    void findAllCampaigns() {
        StepVerifier.create(repositoryImpl.findAll())
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void saveCampaign() {
        campaign.setIdCampaign("3");
        repositoryImpl.saveCampaign(campaign)
                .subscribe(c -> StepVerifier
                        .create(repositoryImpl.findCampaignById(c))
                        .expectNextCount(1)
                        .consumeNextWith(status -> assertEquals(status.getIdCampaign(), campaign.getIdCampaign()))
                        .verifyComplete());
    }

//    @Test
//    void updateCampaign() {
//        campaign.setIdCampaign("1");
//        campaign.setProvider("");
//        campaign.setAttachment(false);
//        campaign.setAttachmentPath(null);
//        StepVerifier.create(repositoryImpl.updateCampaign(campaign))
//                .consumeNextWith(status -> assertNull(status.getActual().getAttachmentPath()))
//                .verifyComplete();
//    }
}
