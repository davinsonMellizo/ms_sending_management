package co.com.bancolombia.usecase.gettemplate;

import co.com.bancolombia.commons.enums.BusinessExceptionEnum;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.template.dto.TemplateRequest;
import co.com.bancolombia.model.template.dto.TemplateResponse;
import co.com.bancolombia.model.template.gateways.TemplateRepository;
import co.com.bancolombia.usecase.SampleData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetTemplateUseCaseTest {

    @InjectMocks
    private GetTemplateUseCase getTemplateUseCase;

    @Mock
    private TemplateRepository templateRepository;

    @BeforeAll
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTemplateSuccessfulTest() {
        Mockito.when(templateRepository.getTemplate(Mockito.anyString()))
                .thenReturn(Mono.just(SampleData.templateResponse()));
        StepVerifier.create(getTemplateUseCase.getTemplate(SampleData.testHeader()))
                .assertNext(templateResponses ->
                        Assertions.assertThat(templateResponses).isInstanceOf(TemplateResponse.class))
                .verifyComplete();
    }

    @Test
    void getTemplateErrorTest() {
        Mockito.when(templateRepository.getTemplate(Mockito.anyString()))
                .thenReturn(Mono.empty());
        StepVerifier.create(getTemplateUseCase.getTemplate(SampleData.testHeader()))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        ((BusinessException) throwable).getException()
                                .equals(BusinessExceptionEnum.TEMPLATE_NOT_FOUND))
                .verify();
    }
}
