package co.com.bancolombia.model.campaign.gateways;


import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.massive.Massive;
import reactor.core.publisher.Mono;

public interface CampaignRepository {
    Mono<Campaign> findCampaignById(Massive massive);
}
