package co.com.bancolombia.campaign;

import co.com.bancolombia.campaign.data.CampaignData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CampaignRepository extends ReactiveCrudRepository<CampaignData, Integer> {

    @Query("SELECT id, id_campaign, id_consumer, id_provider, id_remitter, default_template, description, " +
            "source_path, attachment, attachment_path, state, creation_user, created_date, modified_user, " +
            "modified_date FROM campaign WHERE id_campaign = $1 AND id_consumer = $2")
    Mono<CampaignData> findCampaign(String idCampaign, String idConsumer);

    @Query("DELETE FROM campaign WHERE id_campaign = $1 AND id_consumer = $2")
    Mono<CampaignData> deleteCampaign(String idCampaign, String idConsumer);

}
