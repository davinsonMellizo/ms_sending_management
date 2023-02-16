package co.com.bancolombia.campaign;

import co.com.bancolombia.campaign.data.CampaignData;
import co.com.bancolombia.model.campaign.Campaign;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CampaignRepository extends ReactiveCrudRepository<CampaignData, String> {

    @Query("SELECT id_campaign, id_consumer, provider, id_remitter, default_template, description, source_path, " +
            "attachment, attachment_path, state, creation_user, created_date, modified_user, modified_date, " +
            "data_enrichment, priority FROM campaign WHERE id_campaign = $1 AND id_consumer = $2")
    Mono<CampaignData> findCampaign(String idCampaign, String idConsumer);

    @Query("UPDATE campaign SET id_campaign=:#{#campaign.idCampaign}, id_consumer=:#{#campaign.idConsumer}, " +
            "provider=:#{#campaign.provider}::JSON, id_remitter=:#{#campaign.idRemitter}, " +
            "default_template=:#{#campaign.defaultTemplate}, description=:#{#campaign.description}, " +
            "source_path=:#{#campaign.sourcePath}, attachment=:#{#campaign.attachment}, " +
            "attachment_path=:#{#campaign.attachmentPath}, state=:#{#campaign.state}, " +
            "creation_user=:#{#campaign.creationUser}, created_date=:#{#campaign.createdDate}, " +
            "modified_user=:#{#campaign.modifiedUser}, modified_date=:#{#campaign.modifiedDate}, " +
            "data_enrichment=:#{#campaign.dataEnrichment}, priority=:#{#campaign.priority} " +
            "WHERE id_campaign=:#{#campaign.idCampaign} AND id_consumer=:#{#campaign.idConsumer} " +
            "RETURNING *")
    Mono<CampaignData> updateCampaign(@Param("campaign") Campaign campaign);

    @Query("UPDATE campaign SET state='0', modified_user=:#{#campaign.modifiedUser}, " +
            "modified_date=CURRENT_TIMESTAMP WHERE id_campaign=:#{#campaign.idCampaign} " +
            "AND id_consumer=:#{#campaign.idConsumer}")
    Mono<CampaignData> deleteCampaign(@Param("campaign") Campaign campaign);

}
