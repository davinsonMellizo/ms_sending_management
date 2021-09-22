package co.com.bancolombia.providerservice;

import co.com.bancolombia.model.providerservice.ProviderService;
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
public class ProviderServiceRepositoryImplTest {

    @Autowired
    private ProviderServiceRepositoryImplement repositoryImplement;
    private final ProviderService providerService = new ProviderService();

    @BeforeEach
    public void init() {
    }

    @Test
    public void findAllProviderService() {
        StepVerifier.create(repositoryImplement.findAllProviderService())
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void deleteAlertTransaction() {
        providerService.setIdProvider("JKL");
        providerService.setIdService(1);
        StepVerifier.create(repositoryImplement.deleteProviderService(providerService))
                .consumeNextWith(s -> assertEquals(1, s))
                .verifyComplete();
    }
}