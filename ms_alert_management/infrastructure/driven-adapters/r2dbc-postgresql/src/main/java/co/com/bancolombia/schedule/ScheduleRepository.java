package co.com.bancolombia.schedule;

import co.com.bancolombia.campaign.data.CampaignData;
import co.com.bancolombia.commons.enums.ScheduleType;
import co.com.bancolombia.schedule.data.ScheduleData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ScheduleRepository extends ReactiveCrudRepository<ScheduleData, Long> {

    @Query("SELECT id, id_campaign, id_consumer, schedule_type, start_date, start_time, end_date, end_time, " +
            "creation_user, created_date FROM schedule WHERE id_campaign= $1 AND id_consumer= $2")
    Flux<ScheduleData> findSchedulesByCampaign(String idCampaign, String idConsumer);

    @Query("SELECT id_campaign, id_consumer, provider, id_remitter, default_template, description, source_path, " +
            "attachment, attachment_path, state, creation_user, created_date, modified_user, modified_date, " +
            "data_enrichment, priority FROM campaign WHERE id_campaign = $1 AND id_consumer = $2")
    Mono<CampaignData> findCampaignById(String idCampaign, String idConsumer);

    @Query("SELECT id, id_campaign, id_consumer, schedule_type, start_date, start_time, end_date, end_time, " +
            "creation_user, created_date FROM schedule WHERE id_campaign= $1 AND id_consumer= $2 AND schedule_type= $3")
    Flux<ScheduleData> findSchedulesByTypeByCampaign(String idCampaign, String idConsumer, ScheduleType scheduleType);
}
