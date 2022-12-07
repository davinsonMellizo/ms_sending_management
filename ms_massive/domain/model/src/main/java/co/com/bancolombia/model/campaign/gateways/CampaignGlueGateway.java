package co.com.bancolombia.model.campaign.gateways;

import co.com.bancolombia.model.campaign.Campaign;
import reactor.core.publisher.Mono;


public interface CampaignGlueGateway {

    Mono<String> startTrigger(Campaign response);
}
