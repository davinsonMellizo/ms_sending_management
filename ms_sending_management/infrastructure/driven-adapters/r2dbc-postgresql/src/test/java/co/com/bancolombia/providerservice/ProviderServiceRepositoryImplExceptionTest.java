package co.com.bancolombia.providerservice;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.providerservice.ProviderService;
import co.com.bancolombia.providerservice.data.ProviderServiceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ProviderServiceRepositoryImplExceptionTest {

    @InjectMocks
    private ProviderServiceRepositoryImplement repositoryImpl;
    @Mock
    private ProviderServiceRepository repository;
    @Spy
    private ProviderServiceMapper mapper = Mappers.getMapper(ProviderServiceMapper.class);

    private final ProviderService providerService = new ProviderService();

    @BeforeEach
    public void init() {
        providerService.setId(1);
        providerService.setIdProvider("FGH");
        providerService.setIdService(1);
    }


    @Test
    public void findAllProviderServiceWithException() {
        when(repository.findAllProviderService())
                .thenReturn(Flux.error(RuntimeException::new));
        repositoryImpl.findAllProviderService()
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    public void saveProviderServiceWithException() {
        when(repository.save(any()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.saveProviderService(providerService)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    public void deleteProviderServiceWithException() {
        when(repository.deleteProviderService(anyString(), anyInt()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.deleteProviderService(providerService)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }
}