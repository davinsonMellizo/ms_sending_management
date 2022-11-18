package co.com.bancolombia.glue.adapter;

import co.com.bancolombia.commons.enums.ScheduleType;
import co.com.bancolombia.cronexpression.CronExpression;
import co.com.bancolombia.glue.config.model.GlueConnectionProperties;
import co.com.bancolombia.glue.operations.GlueOperations;
import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.log.LoggerBuilder;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GlueAdapterTest {

    @InjectMocks
    private GlueAdapter glueAdapter;
    @Mock
    private GlueOperations glueOperations;
    @Mock
    private GlueConnectionProperties glueConnectionProperties;
    @Mock
    private CronExpression cronExpression;

    @Mock
    private LoggerBuilder loggerBuilder;
    private static final String GLUE_DATABASE = "glue-database";
    private static final String GLUE_DATABASE_TABLE = "glue-database-table";
    private static final String BUCKET_SOURCE_PATH = "bucket-source-path";
    private static final String BUCKET_DESTINATION_PATH = "bucket-destination-path";
    private static final String GLUE_JOB_NAME = "glue-job-name";
    private static final String GLUE_CRAWLER_NAME = "glue-crawler-name";

    private final Campaign campaign = new Campaign();
    private final Schedule schedule = new Schedule();
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDate DATE_NOW = LocalDate.now();
    private static final LocalTime TIME_NOW = LocalTime.now();


    @BeforeAll
    void init() {
        MockitoAnnotations.openMocks(this);
        when(glueConnectionProperties.getGlueDatabase()).thenReturn(GLUE_DATABASE);
        when(glueConnectionProperties.getGlueDatabaseTable()).thenReturn(GLUE_DATABASE_TABLE);
        when(glueConnectionProperties.getBucketSourcePath()).thenReturn(BUCKET_SOURCE_PATH);
        when(glueConnectionProperties.getBucketDestinationPath()).thenReturn(BUCKET_DESTINATION_PATH);
        when(glueConnectionProperties.getJobName()).thenReturn(GLUE_JOB_NAME);
        when(glueConnectionProperties.getCrawlerName()).thenReturn(GLUE_CRAWLER_NAME);

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
    void createTriggerByScheduleSuccess() {
        when(cronExpression.dateToCron(any(), any(), any(), any()))
                .thenReturn("cron(* * * * * *)");
        when(glueOperations.createTrigger(anyString(), any(), anyString(), any(), anyBoolean()))
                .thenReturn(Mono.just(true));
        StepVerifier.create(glueAdapter.createTrigger(campaign))
                .assertNext(res -> assertEquals(res, campaign))
                .verifyComplete();
    }

    @Test
    void startTriggerByScheduleSuccess() {
        when(glueOperations.startTrigger(anyString()))
                .thenReturn(Mono.just(true));

        StepVerifier.create(glueAdapter.startTrigger(campaign))
                .assertNext(res -> assertEquals(res, campaign))
                .verifyComplete();
    }

    @Test
    void stopTriggerByScheduleSuccess() {
        when(glueOperations.stopTrigger(anyString()))
                .thenReturn(Mono.just(true));

        StepVerifier.create(glueAdapter.stopTrigger(campaign))
                .assertNext(res -> assertEquals(res, campaign))
                .verifyComplete();
    }

}
