package co.com.bancolombia.usecase;

import co.com.bancolombia.model.campaign.gateways.CampaignGateway;
import co.com.bancolombia.model.campaign.gateways.CampaignGlueGateway;
import co.com.bancolombia.model.massive.Massive;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequiredArgsConstructor
public class MassiveUseCase {

    private final CampaignGateway campaignRepository;

    private final CampaignGlueGateway glueGateway;

    public Mono<Map<String, String>> startTriggerOnDemand(Massive massive) {

        return campaignRepository.findCampaignById(massive)
                .doOnNext(campaign -> System.out.println("CAMPAIGN = " + campaign))
                .thenReturn(Map.of("message", "Procesamiento de archivos exitoso"));
    }

}
