package co.com.bancolombia.usecase.schedule;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.campaign.gateways.CampaignGlueGateway;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.model.schedule.Schedule;
import co.com.bancolombia.model.schedule.gateways.ScheduleGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.SCHEDULE_NOT_FOUND;

@RequiredArgsConstructor
public class ScheduleUseCase {
    private final ScheduleGateway scheduleGateway;

    private final CampaignGlueGateway campaignGlueGateway;

    public Mono<Schedule> saveSchedule(Schedule schedule) {
        return scheduleGateway.saveSchedule(schedule)
                .flatMap(campaignGlueGateway::createTrigger)
                .map(campaign -> campaign.getSchedules().get(0));
    }

    public Mono<Schedule> findScheduleById(Long id) {
        return scheduleGateway.findScheduleById(id)
                .switchIfEmpty(Mono.error(new BusinessException(SCHEDULE_NOT_FOUND)));
    }

    public Mono<StatusResponse<Schedule>> updateSchedule(Schedule schedule, Long id) {
        return scheduleGateway.updateSchedule(schedule, id)
                .flatMap(campaignGlueGateway::updateTrigger)
                .map(campaignStatusResponse -> StatusResponse.<Schedule>builder()
                        .before(campaignStatusResponse.getBefore().getSchedules().get(0))
                        .actual(campaignStatusResponse.getActual().getSchedules().get(0))
                        .description(campaignStatusResponse.getDescription())
                        .build()
                );
    }

}
