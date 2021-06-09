package co.com.bancolombia.state;


import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.contact.ContactRepositoryImplement;
import co.com.bancolombia.contact.data.ContactMapper;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.state.data.StateMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class StateRepositoryImplTest {

    @Autowired
    private StateRepositoryImplement stateRepositoryImplement;

    @Test
    public void findStateByName() {
        StepVerifier.create(stateRepositoryImplement.findStateByName("Active"))
                .expectNextCount(1)
                .verifyComplete();
    }
}