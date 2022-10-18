package co.com.bancolombia.schedule;

import co.com.bancolombia.schedule.data.ScheduleData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;


public interface ScheduleRepository extends ReactiveCrudRepository<ScheduleData, Long> {

    @Query("SELECT id, id_campaign, id_consumer, schedule_type, start_date, start_time, end_date, end_time, " +
            "creation_user, created_date FROM schedule WHERE id_campaign = $1 AND id_consumer= $2")
    Flux<ScheduleData> findSchedulesByCampaign(String idCampaign, String idConsumer);

}
