package co.com.bancolombia.usecase;

import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.campaign.gateways.CampaignGateway;
import co.com.bancolombia.model.campaign.gateways.CampaignGlueGateway;
import co.com.bancolombia.model.massive.Massive;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class MassiveUseCase {

    private final CampaignGateway campaignRepository;

    private final CampaignGlueGateway glueGateway;

    public Mono<Campaign> startTriggerOnDemand(Massive massive) {

        return campaignRepository.findCampaignById(massive)
                .flatMap(glueGateway::startTrigger)
                .map(res -> res.toBuilder().build());
    }
}
