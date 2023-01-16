package co.com.bancolombia.consumer.adapter.campaign.mapper;

import co.com.bancolombia.consumer.adapter.campaign.model.SuccessCampaign;
import co.com.bancolombia.model.campaign.Campaign;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ICampaignMapper {
    Campaign toModel(SuccessCampaign.Data data);
}
