package co.com.bancolombia.consumer.mapper;

import co.com.bancolombia.consumer.response.SuccessCampaign;
import co.com.bancolombia.model.campaign.Campaign;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ICampaignMapper {
    Campaign toModel(SuccessCampaign.CampaignResponse data);
}
