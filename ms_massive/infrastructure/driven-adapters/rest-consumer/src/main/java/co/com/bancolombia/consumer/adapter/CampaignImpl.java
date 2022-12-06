package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.commons.exception.BusinessException;
import co.com.bancolombia.commons.exception.TechnicalException;
import co.com.bancolombia.consumer.RestConsumer;
import co.com.bancolombia.consumer.adapter.campaign.mapper.ICampaignMapper;
import co.com.bancolombia.consumer.adapter.campaign.model.ErrorCampaign;
import co.com.bancolombia.consumer.adapter.campaign.model.SuccessCampaign;
import co.com.bancolombia.consumer.config.RestConsumerProperties;
import co.com.bancolombia.consumer.model.Error;
import co.com.bancolombia.consumer.util.Util;
import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.campaign.gateways.CampaignGateway;
import co.com.bancolombia.model.massive.Massive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.BusinessExceptionEnum.BUSINESS_CAMPAIGN_NOT_FOUND;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.TECHNICAL_ERROR_REQUESTING_CAMPAIGN;

@Repository
@RequiredArgsConstructor
public class CampaignImpl implements CampaignGateway {
    private final RestConsumerProperties properties;
    private final RestConsumer restConsumer;
    private final ICampaignMapper campaignMapper;

    @Override
    public Mono<Campaign> findCampaignById(Massive mass) {
        return Util.paramsCampaign(mass.getIdCampaign(), mass.getIdConsumer())
                .flatMap(headers -> restConsumer.get(properties.getResources().getEndpointCampaign(),
                        headers, SuccessCampaign.class, ErrorCampaign.class))
                .map(sc -> this.campaignMapper.toModel(sc.getData()))
                .onErrorMap(e -> e instanceof Error ?
                        new BusinessException(BUSINESS_CAMPAIGN_NOT_FOUND) :
                        new TechnicalException(e.getMessage(), TECHNICAL_ERROR_REQUESTING_CAMPAIGN)
                );
    }
}
