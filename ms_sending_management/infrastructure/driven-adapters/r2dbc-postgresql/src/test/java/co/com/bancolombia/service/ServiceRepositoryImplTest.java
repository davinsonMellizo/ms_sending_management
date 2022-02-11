package co.com.bancolombia.service;

import co.com.bancolombia.model.service.Service;
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
public class ServiceRepositoryImplTest {

    @Autowired
    private ServiceRepositoryImplement repositoryImpl;
    private final Service service = new Service();

    @BeforeEach
    public void init() {
        service.setId(1);
        service.setName("EMAIL BASE");
        service.setState("inactivo");
        service.setCreationUser("User");
    }

    @Test
    public void findIdService() {
        StepVerifier.create(repositoryImpl.findServiceById(service.getId()))
                .consumeNextWith(serviceFound -> assertEquals(service.getId(), serviceFound.getId()))
                .verifyComplete();
    }

    @Test
    public void updateService() {
        StepVerifier.create(repositoryImpl.updateService(service))
                .consumeNextWith(status -> assertEquals(service.getId(), status.getActual().getId()))
                .verifyComplete();
    }

    @Test
    public void saveService() {
        service.setId(2);
        repositoryImpl.saveService(service)
                .subscribe(alertSaved -> StepVerifier
                        .create(repositoryImpl.findServiceById(alertSaved.getId()))
                        .expectNextCount(1)
                        .verifyComplete());
    }

    @Test
    public void deleteService() {
        service.setId(2);
        StepVerifier.create(repositoryImpl.deleteServiceById(service.getId()))
                .consumeNextWith(s -> assertEquals(service.getId(), s))
                .verifyComplete();
    }
}