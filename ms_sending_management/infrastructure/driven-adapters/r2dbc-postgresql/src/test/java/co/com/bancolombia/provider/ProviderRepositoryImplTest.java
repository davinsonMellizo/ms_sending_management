package co.com.bancolombia.provider;

import co.com.bancolombia.model.provider.Provider;
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
class ProviderRepositoryImplTest {

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
    void findIdProvider() {
        StepVerifier.create(repositoryImpl.findProviderById(provider.getId()))
                .consumeNextWith(providerFound -> assertEquals(provider.getId(), providerFound.getId()))
                .verifyComplete();
    }

}