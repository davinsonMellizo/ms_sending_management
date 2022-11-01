package co.com.bancolombia.usecase.campaign;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.campaign.gateways.CampaignGateway;
import co.com.bancolombia.model.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CAMPAIGN_NOT_FOUND;

@RequiredArgsConstructor
public class CampaignUseCase {

    private final CampaignGateway campaignGateway;

    public Mono<List<Campaign>> findAllCampaign() {
        return campaignGateway.findAll().collectList();
    }

    public Mono<Campaign> findCampaignById(Campaign campaign) {
        return campaignGateway.findCampaignById(campaign)
                .switchIfEmpty(Mono.error(new BusinessException(CAMPAIGN_NOT_FOUND)));
    }

    public Mono<Campaign> saveCampaign(Campaign campaign) {
        return campaignGateway.saveCampaign(campaign);
    }

    public Mono<StatusResponse<Campaign>> updateCampaign(Campaign campaign) {
        return campaignGateway.updateCampaign(campaign)
                .switchIfEmpty(Mono.error(new BusinessException(CAMPAIGN_NOT_FOUND)));
    }

    public Mono<String> deleteCampaignById(Campaign campaign) {
        return campaignGateway.findCampaignById(campaign)
                .switchIfEmpty(Mono.error(new BusinessException(CAMPAIGN_NOT_FOUND)))
                .map(c -> c.toBuilder()
                        .modifiedUser(campaign.getModifiedUser())
                        .build())
                .flatMap(campaignGateway::deleteCampaignById);
    }
}
