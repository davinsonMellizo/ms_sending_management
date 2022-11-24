package co.com.bancolombia.glue.adapter;

import co.com.bancolombia.commons.enums.ScheduleType;
import co.com.bancolombia.glue.operations.GlueOperations;
import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.campaign.gateways.CampaignGlueGateway;
import co.com.bancolombia.model.response.StatusResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
public class GlueAdapter implements CampaignGlueGateway {

    private final GlueOperations glueOperations;

    private String getTriggerName(String idCampaign, String idConsumer, Long idSchedule) {
        return String.format("tgr_%s_%s_%s", idCampaign, idConsumer, idSchedule);
    }

    @Override
    public Mono<StatusResponse<Campaign>> startTrigger(StatusResponse<Campaign> response) {
        return Flux.fromIterable(response.getActual().getSchedules())
                .filter(schedule -> ScheduleType.ON_DEMAND.equals(schedule.getScheduleType()))
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
}
