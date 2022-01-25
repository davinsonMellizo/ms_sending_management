package co.com.bancolombia.usecase.deletetemplate;

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
class DeleteTemplateUseCaseTest {

    @InjectMocks
    private DeleteTemplateUseCase deleteTemplateUseCase;

    @Mock
    private TemplateRepository templateRepository;

//    @BeforeAll
//    public void init() {
//        MockitoAnnotations.openMocks(this);
//        Mockito.when(templateRepository.deleteTemplate(Mockito.any()))
//                .thenReturn(Mono.just(SampleData.templateResponse()));
//    }
//
//    @Test
//    void deleteTemplateSuccessfulTest() {
//        Mockito.when(templateRepository.getTemplate(SampleData.templateRequestDelete().getIdTemplate()))
//                .thenReturn(Mono.just(SampleData.templateResponse()));
//        StepVerifier.create(deleteTemplateUseCase.deleteTemplate(SampleData.templateRequestDelete()))
//                .assertNext(templateResponse ->
//                        Assertions.assertThat(templateResponse).isInstanceOf(TemplateResponse.class))
//                .verifyComplete();
//    }
//
//    @Test
//    void createTemplateErrorTest() {
//        Mockito.when(templateRepository.getTemplate(SampleData.templateRequestDelete().getIdTemplate()))
//                .thenReturn(Mono.just(TemplateResponse.builder().build()));
//        StepVerifier.create(deleteTemplateUseCase.deleteTemplate(SampleData.templateRequestDelete()))
//                .expectErrorMatches(throwable ->
//                        throwable instanceof BusinessException &&
//                                ((BusinessException) throwable).getException()
//                                        .equals(BusinessExceptionEnum.TEMPLATE_NOT_FOUND))
//                .verify();
//    }
}
