package co.com.bancolombia.campaign.data;

import co.com.bancolombia.model.campaign.Campaign;
import io.r2dbc.postgresql.codec.Json;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CampaignMapper {

    @Mapping(target = "provider", qualifiedByName = "jsonToString")
    Campaign toEntity(CampaignData campaignData);

    @Mapping(target = "provider", qualifiedByName = "stringToJson")
    CampaignData toData(Campaign campaign);

    @Named("stringToJson")
    static Json stringToJson(String provider) {
        return Json.of(provider);
    }

    @Named("jsonToString")
    static String jsonToString(Json provider) {
        return provider.asString();
    }

}
