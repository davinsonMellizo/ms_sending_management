package co.com.bancolombia.contactmedium;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.contactmedium.data.ContactMediumData;
import co.com.bancolombia.contactmedium.data.ContactMediumMapper;
import co.com.bancolombia.model.contactmedium.ContactMedium;
import co.com.bancolombia.model.contactmedium.gateways.ContactMediumGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ContactMediumRepositoryImplement
        extends AdapterOperations<ContactMedium, ContactMediumData, String, ContactMediumRepository, ContactMediumRepository>
        implements ContactMediumGateway {


    @Autowired
    public ContactMediumRepositoryImplement(ContactMediumRepository repository, ContactMediumMapper mapper) {
        super(repository, repository, null, mapper::toEntity);
    }

    @Override
    public Mono<ContactMedium> findContactMediumByCode(String code) {
        return doQuery(repository.findById(code))
                .onErrorMap(e -> new TechnicalException(e, TechnicalExceptionEnum.FIND_CONTACT_MEDIUM_ERROR));
    }
}
