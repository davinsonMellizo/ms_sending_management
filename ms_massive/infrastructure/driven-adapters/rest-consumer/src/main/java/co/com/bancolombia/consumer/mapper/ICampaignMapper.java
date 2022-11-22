package co.com.bancolombia.consumer.mapper;

import co.com.bancolombia.consumer.model.CampaingResponse;
import co.com.bancolombia.model.campaign.Campaign;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ICampaignMapper {
    Campaign toModel(CampaingResponse data);
}
