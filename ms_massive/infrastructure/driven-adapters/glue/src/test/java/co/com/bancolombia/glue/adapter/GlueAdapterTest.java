package co.com.bancolombia.glue.adapter;

import co.com.bancolombia.commons.enums.ScheduleType;
import co.com.bancolombia.glue.operations.GlueOperations;
import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.schedule.Schedule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GlueAdapterTest {

    @InjectMocks
    private GlueAdapter glueAdapter;
    @Mock
    private GlueOperations glueOperations;
    private final Campaign campaign = new Campaign();
    private final Schedule schedule = new Schedule();
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDate DATE_NOW = LocalDate.now();
    private static final LocalTime TIME_NOW = LocalTime.now();


    @BeforeAll
    void init() {
        MockitoAnnotations.openMocks(this);

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
        campaign.setCreationUser("Nasuarez");

        schedule.setIdCampaign("1");
        schedule.setIdConsumer("ALM");
        schedule.setId(45L);
        schedule.setScheduleType(ScheduleType.ON_DEMAND);
        schedule.setStartDate(DATE_NOW);
        schedule.setStartTime(TIME_NOW);
        schedule.setEndDate(DATE_NOW.plusMonths(1));
        schedule.setEndTime(TIME_NOW);

        campaign.setSchedules(List.of(schedule));
    }


    @Test
    void startTriggerByScheduleSuccess() {
        when(glueOperations.startTrigger(anyString()))
                .thenReturn(Mono.just(true));
        StepVerifier.create(glueAdapter.startTrigger(campaign))
                .assertNext(res -> assertEquals(res, "tgr_1_ALM_45"))
                .verifyComplete();
    }

}
