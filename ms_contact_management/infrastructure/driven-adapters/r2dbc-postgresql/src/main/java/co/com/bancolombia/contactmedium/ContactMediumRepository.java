package co.com.bancolombia.contactmedium;

import co.com.bancolombia.contactmedium.data.ContactMediumData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ContactMediumRepository extends ReactiveCrudRepository<ContactMediumData, String> {

}
