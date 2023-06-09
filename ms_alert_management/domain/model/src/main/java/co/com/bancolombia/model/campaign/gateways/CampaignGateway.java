package co.com.bancolombia.model.campaign.gateways;

import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.response.StatusResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface CampaignGateway {

    Flux<Campaign> findAll();

    Mono<Campaign> findCampaignById(Campaign campaign);

    Mono<Campaign> saveCampaign(Campaign campaign);

    Mono<StatusResponse<Campaign>> updateCampaign(Campaign campaign);

    Mono<Map<String, String>> deleteCampaignById(Campaign campaign);

}
