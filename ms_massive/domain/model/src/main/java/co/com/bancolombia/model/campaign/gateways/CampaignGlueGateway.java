package co.com.bancolombia.model.campaign.gateways;

import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.response.StatusResponse;
import reactor.core.publisher.Mono;

public interface CampaignGlueGateway {

    Mono<StatusResponse<Campaign>> startTrigger(StatusResponse<Campaign> response);
}
