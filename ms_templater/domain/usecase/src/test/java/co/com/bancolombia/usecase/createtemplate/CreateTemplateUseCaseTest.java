package co.com.bancolombia.usecase.createtemplate;

import co.com.bancolombia.commons.enums.BusinessExceptionEnum;
import co.com.bancolombia.commons.exceptions.BusinessException;
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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateTemplateUseCaseTest {

    @InjectMocks
    private CreateTemplateUseCase createTemplateUseCase;

    @Mock
    private TemplateRepository templateRepository;

//    @BeforeAll
//    public void init() {
//        MockitoAnnotations.openMocks(this);
//        Mockito.when(templateRepository.createTemplate(Mockito.any()))
//                .thenReturn(Mono.just(SampleData.templateResponse()));
//    }
//
//    @Test
//    void createTemplateSuccessfulTest() {
//        Mockito.when(templateRepository.getTemplate(SampleData.templateRequest().getIdTemplate()))
//                .thenReturn(Mono.just(TemplateResponse.builder().build()));
//        StepVerifier.create(createTemplateUseCase.createTemplate(SampleData.templateRequest()))
//                .assertNext(templateResponse ->
//                        Assertions.assertThat(templateResponse).isInstanceOf(TemplateResponse.class))
//                .verifyComplete();
//    }
//
//    @Test
//    void createTemplateErrorTest() {
//        Mockito.when(templateRepository.getTemplate(SampleData.templateRequest().getIdTemplate()))
//                .thenReturn(Mono.just(SampleData.templateResponse()));
//        StepVerifier.create(createTemplateUseCase.createTemplate(SampleData.templateRequest()))
//                .expectErrorMatches(throwable ->
//                        throwable instanceof BusinessException &&
//                                ((BusinessException) throwable).getException()
//                                        .equals(BusinessExceptionEnum.TEMPLATE_ALREADY_EXISTS))
//                .verify();
//    }
}
