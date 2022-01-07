package co.com.bancolombia.usecase.updatetemplate;

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

import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UpdateTemplateUseCaseTest {

    @InjectMocks
    private UpdateTemplateUseCase updateTemplateUseCase;

    @Mock
    private TemplateRepository templateRepository;

    @BeforeAll
    public void init() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(templateRepository.updateTemplate(Mockito.any()))
                .thenReturn(Mono.just(SampleData.templateResponse()));
        Mockito.when(templateRepository.getTemplate(SampleData.templateRequestUpdate().getId(), null, null))
                .thenReturn(Flux.just(SampleData.templateResponse()));
    }

    @Test
    void updateTemplateSuccessfulTest() {
        StepVerifier.create(updateTemplateUseCase.updateTemplate(SampleData.templateRequestUpdate()))
                .assertNext(templateResponseMap ->
                        Assertions.assertThat(templateResponseMap).isInstanceOf(Map.class))
                .verifyComplete();
    }
}
