package co.com.bancolombia.alerttemplate;

import co.com.bancolombia.alerttemplate.data.AlertTemplateData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface AlertTemplateRepository extends ReactiveCrudRepository<AlertTemplateData, String> {
    @Query("select * from alert_template where id = $1")
    Flux<AlertTemplateData> findTemplateById(String id);
}
