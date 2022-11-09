package co.com.bancolombia.model.campaign.gateways;

import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.response.StatusResponse;
import reactor.core.publisher.Mono;

public interface CampaignGlueGateway {
    Mono<Campaign> campaignCreateTrigger(Campaign campaign);
    Mono<Campaign> campaignStartTrigger(Campaign campaign);
    Mono<Campaign> campaignStopTrigger(Campaign campaign);
    Mono<StatusResponse<Campaign>> campaignUpdateTrigger(StatusResponse<Campaign> response);
}
