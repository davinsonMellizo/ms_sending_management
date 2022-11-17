package co.com.bancolombia.usecase.campaign;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.campaign.gateways.CampaignGateway;
import co.com.bancolombia.model.campaign.gateways.CampaignGlueGateway;
import co.com.bancolombia.model.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CAMPAIGN_NOT_FOUND;

@RequiredArgsConstructor
public class CampaignUseCase {

    private final CampaignGateway campaignGateway;
    private final CampaignGlueGateway glueGateway;

    public Mono<List<Campaign>> findAllCampaign() {
        return campaignGateway.findAll().collectList();
    }

    public Mono<Campaign> findCampaignById(Campaign campaign) {
        return campaignGateway.findCampaignById(campaign)
                .switchIfEmpty(Mono.error(new BusinessException(CAMPAIGN_NOT_FOUND)));
    }

    public Mono<Campaign> saveCampaign(Campaign campaign) {
        return campaignGateway.saveCampaign(campaign)
                .flatMap(glueGateway::createTrigger);
    }

    public Mono<StatusResponse<Campaign>> updateCampaign(Campaign campaign) {
        return campaignGateway.updateCampaign(campaign)
                .switchIfEmpty(Mono.error(new BusinessException(CAMPAIGN_NOT_FOUND)))
                .flatMap(glueGateway::updateTrigger)
                .map(res -> {
                    if (!res.getActual().getState().equals(res.getBefore().getState())) {
                        glueGateway.startTrigger(res.getActual());
                    }
                    return res.toBuilder()
                            .before(res.getBefore().toBuilder().schedules(null).build())
                            .actual(res.getActual().toBuilder().schedules(null).build())
                            .build();
                });
    }

    public Mono<Map<String, String>> deleteCampaignById(Campaign campaign) {
        return this.findCampaignById(campaign)
                .map(c -> c.toBuilder().modifiedUser(campaign.getModifiedUser()).build())
                .flatMap(glueGateway::stopTrigger)
                .flatMap(campaignGateway::deleteCampaignById);
    }
}
