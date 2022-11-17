package co.com.bancolombia.model.schedule.gateways;

import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.model.schedule.Schedule;
import reactor.core.publisher.Mono;


public interface ScheduleGateway {

    Mono<Campaign> saveSchedule(Schedule schedule);

    Mono<StatusResponse<Campaign>> updateSchedule(Schedule schedule, Long id);

    Mono<Schedule> findScheduleById(Long id);

    Mono<Campaign> saveSchedulesByCampaign(Campaign campaign);

    Mono<Campaign> findSchedulesByCampaign(Campaign campaign);

}
