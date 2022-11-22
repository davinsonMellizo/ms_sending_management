package co.com.bancolombia.usecase;

import co.com.bancolombia.model.campaign.gateways.CampaignRepository;
import co.com.bancolombia.model.massive.Massive;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequiredArgsConstructor
public class MassiveUseCase {

    private final CampaignRepository campaignRepository;

    public Mono<Map<String, String>> startTriggerOnDemand(Massive massive) {

        return campaignRepository.findCampaignById(massive)
                .doOnNext(campaign -> System.out.println("CAM = " + campaign))
                .thenReturn(Map.of("message", "Procesamiento de archivos exitoso"));
    }

}
