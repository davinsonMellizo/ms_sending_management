package co.com.bancolombia.consumer.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@UtilityClass
public class Util {
    private static final String ID_CAMPAIGN = "id-campaign";
    private static final String ID_CONSUMER = "id-consumer";

    public static MultiValueMap<String, String> paramsCampaign(String idCampaign, String idConsumer) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add(ID_CAMPAIGN, idCampaign);
        queryParams.add(ID_CONSUMER, idConsumer);
        return queryParams;
    }
}
