package co.com.bancolombia.usecase.gettemplate;

import co.com.bancolombia.commons.enums.BusinessExceptionEnum;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.template.dto.Template;
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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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
                .thenReturn(Mono.just(SampleData.template()));
        StepVerifier.create(getTemplateUseCase.getTemplate(SampleData.getRequest()))
                .assertNext(templateResponses ->
                        Assertions.assertThat(templateResponses).isInstanceOf(Template.class))
                .verifyComplete();
    }

    @Test
    void getTemplateErrorTest() {
        Mockito.when(templateRepository.getTemplate(Mockito.anyString()))
                .thenReturn(Mono.empty());
        StepVerifier.create(getTemplateUseCase.getTemplate(SampleData.getRequest()))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        ((BusinessException) throwable).getException()
                                .equals(BusinessExceptionEnum.TEMPLATE_NOT_FOUND))
                .verify();
    }
}
