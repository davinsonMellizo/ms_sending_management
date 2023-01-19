package co.com.bancolombia.usecase.log;

import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.newness.Newness;
import co.com.bancolombia.model.newness.gateways.NewnessRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewnessUseCaseTest {
    @InjectMocks
    private NewnessUseCase useCase;
    @Mock
    private NewnessRepository newnessRepository;

    private final Client client = new Client();
    private final Contact contact = new Contact();

    @BeforeEach
    public void init() {
        client.setDocumentNumber(1061772353L);
        client.setDocumentType("0");
        client.setIdState(1);
        client.setConsumerCode("SVP");
        contact.setDocumentNumber(1061772353L);
        contact.setDocumentType("0");
        contact.setStateContact("1");
    }

    @Test
    void saveNewnessClient(){
        when(newnessRepository.saveNewness(any()))
                .thenReturn(Mono.just(new Newness()));
        StepVerifier
                .create(useCase.saveNewness(client, "sss", "1"))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void saveNewnessContact(){
        when(newnessRepository.saveNewness(any()))
                .thenReturn(Mono.just(new Newness()));
        StepVerifier
                .create(useCase.saveNewness(contact, "sss", "1"))
                .expectNextCount(1)
                .verifyComplete();
    }
}
