package co.com.bancolombia.contact.writer;

import co.com.bancolombia.contact.data.ContactData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ContactRepository extends ReactiveCrudRepository<ContactData, Integer> {

}
