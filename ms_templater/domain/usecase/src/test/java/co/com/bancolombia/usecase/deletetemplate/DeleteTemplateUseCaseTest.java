package co.com.bancolombia.usecase.deletetemplate;

import co.com.bancolombia.model.template.dto.UpdateTemplateResponse;
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
class DeleteTemplateUseCaseTest {

    @InjectMocks
    private DeleteTemplateUseCase deleteTemplateUseCase;

    @Mock
    private TemplateRepository templateRepository;

    @BeforeAll
    public void init() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(templateRepository.saveTemplate(Mockito.any()))
                .thenReturn(Mono.just(SampleData.template().toBuilder().status("0").build()));
        Mockito.when(templateRepository.getTemplate(SampleData.templateRequestUpdate().getIdTemplate()))
                .thenReturn(Mono.just(SampleData.template()));
    }

    @Test
    void deleteTemplateSuccessfulTest() {
        StepVerifier.create(deleteTemplateUseCase.deleteTemplate(SampleData.templateRequestDelete()))
                .assertNext(updateTemplateResponse ->
                        Assertions.assertThat(updateTemplateResponse).isInstanceOf(UpdateTemplateResponse.class))
                .verifyComplete();
    }

}
