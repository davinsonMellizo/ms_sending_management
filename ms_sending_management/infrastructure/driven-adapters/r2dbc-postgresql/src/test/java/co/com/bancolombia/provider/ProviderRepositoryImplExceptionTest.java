package co.com.bancolombia.provider;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.provider.Provider;
import co.com.bancolombia.provider.data.ProviderData;
import co.com.bancolombia.provider.data.ProviderMapper;
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

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ProviderRepositoryImplExceptionTest {

    @InjectMocks
    private ProviderRepositoryImplement repositoryImpl;
    @Mock
    private ProviderRepository repository;
    @Spy
    private ProviderMapper mapper = Mappers.getMapper(ProviderMapper.class);
    private static final LocalDateTime NOW = LocalDateTime.now();
    @Mock
    private TimeFactory timeFactory;

    private final Provider provider = new Provider();
    private final ProviderData providerData = new ProviderData();

    @BeforeEach
    public void init() {
        provider.setId("HJK");
        provider.setName("TODO1");
        provider.setTypeService("A");
        providerData.setId("HJK");
        providerData.setName("TODO1");
        providerData.setTypeService("A");
    }


    @Test
    public void findProviderByIdWithException() {
        when(repository.findById(anyString()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.findProviderById(provider.getId())
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    public void findAllProviderWithException() {
        when(repository.findAll())
                .thenReturn(Flux.error(RuntimeException::new));
        repositoryImpl.findAll()
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    public void saveProviderWithException() {
        when(timeFactory.now()).thenReturn(NOW);
        when(repository.save(any()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.saveProvider(provider)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    public void updateRemitterWithException() {
        when(repository.save(any()))
                .thenReturn(Mono.error(RuntimeException::new));
        when(repository.findById(anyString()))
                .thenReturn(Mono.just(providerData));
        repositoryImpl.updateProvider(provider)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    public void deleteRemitterWithException() {
        when(repository.deleteById(anyString()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.deleteProviderById(provider.getId())
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }
}