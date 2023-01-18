package co.com.bancolombia.client;


import co.com.bancolombia.client.data.ClientMapper;
import co.com.bancolombia.client.reader.IClientRepositoryReader;
import co.com.bancolombia.client.writer.IClientRepository;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.response.StatusResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class IClientRepositoryImplExceptionTest {

    @InjectMocks
    private ClientRepositoryImplement repositoryImpl;
    @Mock
    private IClientRepositoryReader repositoryReader;
    @Mock
    private IClientRepository repository;

    @Spy
    private ClientMapper mapper = Mappers.getMapper(ClientMapper.class);
    private static final LocalDateTime NOW = LocalDateTime.now();
    @Mock
    private TimeFactory timeFactory;

    private final Client client = new Client();

    @BeforeEach
    public void init() {
        client.setDocumentNumber(1061772353L);
        client.setDocumentType("0");
        client.setIdState(0);
        client.setCreationUser("username");
        client.setEnrollmentOrigin("ALM");
        client.setKeyMdm("key");
    }


    @Test
    void findClientByDocument() {
        when(repositoryReader.findClientByIdentification(any(), any()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.findClientByIdentification(client)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void updateContact() {
        when(timeFactory.now()).thenReturn(NOW);
        when(repository.save(any()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.updateClient(StatusResponse.<Client>builder()
                .before(client).actual(client).build())
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }
}