package co.com.bancolombia.glue.adapter;

import co.com.bancolombia.commons.enums.ScheduleType;
import co.com.bancolombia.cronexpression.CronExpression;
import co.com.bancolombia.glue.config.model.GlueConnectionProperties;
import co.com.bancolombia.glue.operations.GlueOperations;
import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.campaign.gateways.CampaignGlueGateway;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.model.schedule.Schedule;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.glue.model.Action;
import software.amazon.awssdk.services.glue.model.TriggerType;
import software.amazon.awssdk.services.glue.model.TriggerUpdate;

import java.util.Map;

@Repository
@AllArgsConstructor
public class GlueAdapter implements CampaignGlueGateway {

    @Autowired
    private CronExpression cronExpression;

    private final GlueConnectionProperties properties;
    private final GlueOperations glueOperations;

    private static final String GLUE_DATABASE = "--glue_database";
    private static final String GLUE_DATABASE_TABLE = "--glue_database_table";
    private static final String SOURCE_MASSIVE_FILE = "--source_massive_file_path";
    private static final String BUCKET_DESTINATION_PATH = "--bucket_destination_path";
    private static final String DATA_ENRICHMENT = "--data_enrichment";
    private static final String CONSUMER_ID = "--consumer_id";

    private String getTriggerName(String idCampaign, String idConsumer, Long idSchedule) {
        return String.format("tgr_%s_%s_%s", idCampaign, idConsumer, idSchedule);
    }

    private TriggerType getTriggerType(ScheduleType scheduleType) {
        return ScheduleType.ON_DEMAND.equals(scheduleType) ? TriggerType.ON_DEMAND : TriggerType.SCHEDULED;
    }

    private String getCronExpression(Schedule schedule) {
        return !ScheduleType.ON_DEMAND.equals(schedule.getScheduleType()) ? cronExpression.dateToCron(
                schedule.getScheduleType(), schedule.getStartDate(),
                schedule.getStartTime(), schedule.getEndDate()
        ) : null;
    }

    private String getBucketDestinationPath(String sourcePath) {
        return sourcePath.replaceAll("(?i).csv", "/");
    }

    private Action getTriggerAction(Campaign campaign) {
        return Action
                .builder()
                .jobName(properties.getJobName())
                .arguments(Map.of(
                        GLUE_DATABASE, properties.getGlueDatabase(),
                        GLUE_DATABASE_TABLE, properties.getGlueDatabaseTable(),
                        SOURCE_MASSIVE_FILE, String.join("/", properties.getBucketSourcePath(),
                                campaign.getSourcePath()),
                        BUCKET_DESTINATION_PATH, String.join("/", properties.getBucketDestinationPath(),
                                this.getBucketDestinationPath(campaign.getSourcePath())),
                        DATA_ENRICHMENT, campaign.getDataEnrichment().toString(),
                        CONSUMER_ID, campaign.getIdConsumer()
                ))
                .build();
    }

    @Override
    public Mono<Campaign> createTrigger(Campaign campaign) {
        return Flux.fromIterable(campaign.getSchedules())
                .flatMap(schedule -> this.glueOperations.createTrigger(
                        this.getTriggerName(campaign.getIdCampaign(), campaign.getIdConsumer(), schedule.getId()),
                        this.getTriggerType(schedule.getScheduleType()),
                        this.getCronExpression(schedule),
                        this.getTriggerAction(campaign),
                        !ScheduleType.ON_DEMAND.equals(schedule.getScheduleType()) ? true : null
                ))
                .collectList()
                .thenReturn(campaign);
    }

    @Override
    public Mono<StatusResponse<Campaign>> startTrigger(StatusResponse<Campaign> response) {
        return Flux.fromIterable(response.getActual().getSchedules())
                .filter(schedule -> !ScheduleType.ON_DEMAND.equals(schedule.getScheduleType()))
                .flatMap(schedule -> this.glueOperations.startTrigger(
                        this.getTriggerName(
                                response.getActual().getIdCampaign(),
                                response.getActual().getIdConsumer(),
                                schedule.getId()
                        ))
                )
                .collectList()
                .thenReturn(response);
    }

    @Override
    public Mono<Campaign> stopTrigger(Campaign campaign) {
        return Flux.fromIterable(campaign.getSchedules())
                .filter(schedule -> !ScheduleType.ON_DEMAND.equals(schedule.getScheduleType()))
                .flatMap(schedule -> this.glueOperations.stopTrigger(
                        this.getTriggerName(campaign.getIdCampaign(), campaign.getIdConsumer(), schedule.getId())
                ))
                .collectList()
                .thenReturn(campaign);
    }

    @Override
    public Mono<StatusResponse<Campaign>> updateTrigger(StatusResponse<Campaign> response) {
        return Flux.fromIterable(response.getActual().getSchedules())
                .flatMap(schedule -> this.glueOperations.updateTrigger(
                        this.getTriggerName(
                                response.getActual().getIdCampaign(),
                                response.getActual().getIdConsumer(),
                                schedule.getId()
                        ),
                        TriggerUpdate
                                .builder()
                                .schedule(this.getCronExpression(schedule))
                                .actions(this.getTriggerAction(response.getActual()))
                                .build()
                ))
                .collectList()
                .thenReturn(response);
    }

    @Override
    public Mono<String> deleteTrigger(String nameTrigger) {
        return  this.glueOperations.deleteTrigger(nameTrigger)
                .thenReturn(nameTrigger);
    }


}
