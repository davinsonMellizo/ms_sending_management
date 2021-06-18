package co.com.bancolombia.provider;

import co.com.bancolombia.model.provider.Provider;
import co.com.bancolombia.provider.ProviderRepositoryImplement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProviderRepositoryImplTest {

    @Autowired
    private ProviderRepositoryImplement repositoryImpl;
    private final Provider provider = new Provider();

    @BeforeEach
    public void init() {
        provider.setId("HJK");
        provider.setName("TODO1");
        provider.setTypeService("A");
    }

    @Test
    public void findIdProvider() {
        StepVerifier.create(repositoryImpl.findProviderById(provider.getId()))
                .consumeNextWith(providerFound -> assertEquals(provider.getId(), providerFound.getId()))
                .verifyComplete();
    }

    @Test
    public void updateProvider() {
        StepVerifier.create(repositoryImpl.updateProvider(provider))
                .consumeNextWith(status -> assertEquals(provider.getId(), status.getActual().getId()))
                .verifyComplete();
    }

    @Test
    public void saveProvider() {
        provider.setId("GHJ");
        repositoryImpl.saveProvider(provider)
                .subscribe(alertSaved -> StepVerifier
                        .create(repositoryImpl.findProviderById(alertSaved.getId()))
                        .expectNextCount(1)
                        .verifyComplete());
    }

    @Test
    public void deleteProvider() {
        provider.setId("FGH");
        StepVerifier.create(repositoryImpl.deleteProviderById(provider.getId()))
                .consumeNextWith(s -> assertEquals(provider.getId(), s))
                .verifyComplete();
    }
}