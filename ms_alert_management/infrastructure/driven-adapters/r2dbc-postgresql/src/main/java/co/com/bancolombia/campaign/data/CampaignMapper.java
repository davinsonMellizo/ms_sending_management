package co.com.bancolombia.campaign.data;

import co.com.bancolombia.model.campaign.Campaign;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CampaignMapper {

    Campaign toEntity(CampaignData campaignData);

    @Mapping(target = "isNew", ignore = true)
    CampaignData toData(Campaign campaign);

}
