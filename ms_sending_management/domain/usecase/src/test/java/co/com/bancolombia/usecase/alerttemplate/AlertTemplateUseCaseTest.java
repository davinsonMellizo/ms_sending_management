package co.com.bancolombia.usecase.alerttemplate;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.config.model.alerttemplate.AlertTemplate;
import co.com.bancolombia.config.model.alerttemplate.gateways.AlertTemplateGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AlertTemplateUseCaseTest {

    @InjectMocks
    private AlertTemplateUseCase useCase;

    @Mock
    private AlertTemplateGateway alertTemplateGateway;

    private final AlertTemplate alertTemplate = new AlertTemplate();

    @BeforeEach
    public void init() {
        alertTemplate.setId(1);
    }

    @Test
    public void findAlertTemplateById() {
        when(alertTemplateGateway.findTemplateById(anyInt()))
                .thenReturn(Mono.just(alertTemplate));
        StepVerifier
                .create(useCase.findAlertTemplateById(alertTemplate.getId()))
                .expectNextCount(1)
                .verifyComplete();
        verify(alertTemplateGateway).findTemplateById(anyInt());
    }

    @Test
    public void saveAlertTemplate() {
        when(alertTemplateGateway.save(any()))
                .thenReturn(Mono.just(alertTemplate));
        StepVerifier
                .create(useCase.saveAlertTemplate(alertTemplate))
                .assertNext(response -> response
                        .getId().equals(alertTemplate.getId()))
                .verifyComplete();
        verify(alertTemplateGateway).save(any());
    }

    @Test
    public void deleteAlertTemplate() {
        when(alertTemplateGateway.findTemplateById(anyInt()))
                .thenReturn(Mono.just(alertTemplate));
        when(alertTemplateGateway.delete(any()))
                .thenReturn(Mono.just(alertTemplate.getId()));
        StepVerifier.create(useCase.deleteAlertTemplateById(alertTemplate.getId()))
                .expectNextCount(1)
                .verifyComplete();
        verify(alertTemplateGateway).delete(any());
    }

    @Test
    public void deleteAlertTemplateWithException() {
        when(alertTemplateGateway.findTemplateById(anyInt()))
                .thenReturn(Mono.empty());
        useCase.deleteAlertTemplateById(alertTemplate.getId())
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }
}