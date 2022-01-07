package co.com.bancolombia.usecase.gettemplate;

import co.com.bancolombia.commons.enums.BusinessExceptionEnum;
import co.com.bancolombia.commons.exceptions.BusinessException;
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
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetTemplateUseCaseTest {

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
        Mockito.when(templateRepository.getTemplate(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Flux.just(SampleData.templateResponse()));
        StepVerifier.create(getTemplateUseCase.getTemplate(SampleData.testHeader()))
                .assertNext(categoryResponses ->
                        Assertions.assertThat(categoryResponses).isInstanceOf(ArrayList.class))
                .verifyComplete();
    }

    @Test
    void getCategoryErrorTest() {
        Mockito.when(templateRepository.getTemplate(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Flux.just());
        StepVerifier.create(getTemplateUseCase.getTemplate(SampleData.testHeader()))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        ((BusinessException) throwable).getException()
                                .equals(BusinessExceptionEnum.TEMPLATE_NOT_FOUND))
                .verify();
    }
}
