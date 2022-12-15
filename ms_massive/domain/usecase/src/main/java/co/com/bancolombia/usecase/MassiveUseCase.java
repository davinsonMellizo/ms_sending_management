package co.com.bancolombia.usecase;

import co.com.bancolombia.model.campaign.gateways.CampaignGateway;
import co.com.bancolombia.model.campaign.gateways.CampaignGlueGateway;
import co.com.bancolombia.model.commons.exception.BusinessException;
import co.com.bancolombia.model.massive.Massive;
import co.com.bancolombia.model.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.model.commons.enums.BusinessExceptionEnum.BUSINESS_CAMPAIGN_IS_INACTIVE;

@RequiredArgsConstructor
public class MassiveUseCase {

    private final CampaignGateway campaignRepository;

    private final CampaignGlueGateway glueGateway;

    public Mono<StatusResponse> sendCampaign(Massive massive) {
        return campaignRepository.findCampaignById(massive)
                .filter(campaign -> campaign.getState().equals("1"))
                .switchIfEmpty(Mono.error(new BusinessException(BUSINESS_CAMPAIGN_IS_INACTIVE)))
                .flatMap(glueGateway::startTrigger)
                .map(triggerName -> StatusResponse
                        .builder()
                        .message("Campana en ejecucion")
                        .triggerName(triggerName)
                        .build());
    }
}
