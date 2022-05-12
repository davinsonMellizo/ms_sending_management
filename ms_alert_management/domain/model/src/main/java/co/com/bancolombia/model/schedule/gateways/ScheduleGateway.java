package co.com.bancolombia.model.schedule.gateways;

import co.com.bancolombia.model.campaign.Campaign;
import reactor.core.publisher.Mono;


public interface ScheduleGateway {

    Mono<Campaign> saveSchedulesByCampaign(Campaign campaign);

    Mono<Campaign> updateSchedulesByCampaign(Campaign campaign);

    Mono<Campaign> findSchedulesByCampaign(Campaign campaign);

    Mono<String> deleteSchedulesByCampaign(Campaign campaign);

}
