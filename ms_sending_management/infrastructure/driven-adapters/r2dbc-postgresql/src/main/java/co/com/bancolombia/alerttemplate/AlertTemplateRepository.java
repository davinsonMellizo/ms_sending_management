package co.com.bancolombia.alerttemplate;

import co.com.bancolombia.alerttemplate.data.AlertTemplateData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AlertTemplateRepository extends ReactiveCrudRepository<AlertTemplateData, Integer> {
}
