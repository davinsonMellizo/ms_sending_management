package co.com.bancolombia.model.campaign.gateways;

import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.response.StatusResponse;
import reactor.core.publisher.Mono;

public interface CampaignGlueGateway {
    Mono<Campaign> createTrigger(Campaign campaign);

    Mono<Campaign> startTrigger(Campaign campaign);

    Mono<Campaign> stopTrigger(Campaign campaign);

    Mono<StatusResponse<Campaign>> updateTrigger(StatusResponse<Campaign> response);
}
