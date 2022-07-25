package co.com.bancolombia.usecase.createmessage;

import co.com.bancolombia.commons.enums.BusinessExceptionEnum;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.template.dto.MessageResponse;
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
class CreateMessageUseCaseTest {

    @InjectMocks
    private CreateMessageUseCase createMessageUseCase;

    @Mock
    private TemplateRepository templateRepository;

    @BeforeAll
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createMessageSuccessfulTest() {
        Mockito.when(templateRepository.getTemplate(Mockito.anyString()))
                .thenReturn(Mono.just(SampleData.template()));
        StepVerifier.create(createMessageUseCase.createMessage(SampleData.messageRequest()))
                .assertNext(templateResponses ->
                        Assertions.assertThat(templateResponses).isInstanceOf(MessageResponse.class))
                .verifyComplete();
    }

    @Test
    void createMessageErrorTest() {
        Mockito.when(templateRepository.getTemplate(Mockito.anyString()))
                .thenReturn(Mono.empty());
        StepVerifier.create(createMessageUseCase.createMessage(SampleData.messageRequest()))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        ((BusinessException) throwable).getException()
                                .equals(BusinessExceptionEnum.TEMPLATE_NOT_FOUND))
                .verify();
    }
}
