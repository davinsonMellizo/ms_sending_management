package co.com.bancolombia.usecase.createtemplate;

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
class CreateTemplateUseCaseTest {

    @InjectMocks
    private CreateTemplateUseCase createTemplateUseCase;

    @Mock
    private TemplateRepository templateRepository;

    @BeforeAll
    public void init() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(templateRepository.saveTemplate(Mockito.any()))
                .thenReturn(Mono.just(SampleData.template()));
    }

    @Test
    void createTemplateSuccessfulTest() {
        Mockito.when(templateRepository.getTemplate(Mockito.anyString()))
                .thenReturn(Mono.empty());
        StepVerifier.create(createTemplateUseCase.createTemplate(SampleData.template()))
                .assertNext(templateResponse ->
                        Assertions.assertThat(templateResponse).isInstanceOf(Template.class))
                .verifyComplete();
    }

    @Test
    void createTemplateErrorTest() {
        Mockito.when(templateRepository.getTemplate(Mockito.anyString()))
                .thenReturn(Mono.just(SampleData.template()));
        StepVerifier.create(createTemplateUseCase.createTemplate(SampleData.template()))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                ((BusinessException) throwable).getException()
                                        .equals(BusinessExceptionEnum.TEMPLATE_ALREADY_EXISTS))
                .verify();
    }
}
