package co.com.bancolombia.glue.adapter;

import co.com.bancolombia.commons.enums.ScheduleType;
import co.com.bancolombia.commons.exception.BusinessException;
import co.com.bancolombia.glue.operations.GlueOperations;
import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.campaign.gateways.CampaignGlueGateway;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.BusinessExceptionEnum.BUSINESS_CAMPAIGN_WITHOUT_SCHEDULE_ON_DEMAND;

@Repository
@AllArgsConstructor
public class GlueAdapter implements CampaignGlueGateway {

    private final GlueOperations glueOperations;

    private String getTriggerName(String idCampaign, String idConsumer, Long idSchedule) {
        return String.format("tgr_%s_%s_%s", idCampaign, idConsumer, idSchedule);
    }

    @Override
    public Mono<String> startTrigger(Campaign campaign) {
        return Flux.fromIterable(campaign.getSchedules())
                .filter(schedule -> ScheduleType.ON_DEMAND.equals(schedule.getScheduleType()))
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new BusinessException(BUSINESS_CAMPAIGN_WITHOUT_SCHEDULE_ON_DEMAND))))
                .map(schedule -> this.getTriggerName(
                        campaign.getIdCampaign(),
                        campaign.getIdConsumer(),
                        schedule.getId()))
                .next()
                .flatMap(triggerName -> this.glueOperations
                        .startTrigger(triggerName)
                        .thenReturn(triggerName)
                );
    }
}
