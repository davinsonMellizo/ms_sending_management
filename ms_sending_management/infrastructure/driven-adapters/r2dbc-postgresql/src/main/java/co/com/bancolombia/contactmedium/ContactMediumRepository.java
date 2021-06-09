package co.com.bancolombia.contactmedium;

import co.com.bancolombia.contactmedium.data.ContactMediumData;
import co.com.bancolombia.state.data.StateData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ContactMediumRepository extends ReactiveCrudRepository<ContactMediumData, String> {

}
