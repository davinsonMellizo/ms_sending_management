package co.com.bancolombia.api;

import co.com.bancolombia.api.commons.RequestValidator;
import co.com.bancolombia.api.dto.DeleteTemplateDTO;
import co.com.bancolombia.api.dto.TemplateDTO;
import co.com.bancolombia.commons.enums.BusinessExceptionEnum;
import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.template.dto.Template;
import co.com.bancolombia.usecase.createmessage.CreateMessageUseCase;
import co.com.bancolombia.usecase.createtemplate.CreateTemplateUseCase;
import co.com.bancolombia.usecase.deletetemplate.DeleteTemplateUseCase;
import co.com.bancolombia.usecase.gettemplate.GetTemplateUseCase;
import co.com.bancolombia.usecase.updatetemplate.UpdateTemplateUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HandlerTest {

    @Mock
    private ServerRequest request;

    @Mock
    private RequestValidator validator;

    @Mock
    private CreateTemplateUseCase createTemplateUseCase;

    @Mock
    private GetTemplateUseCase getTemplateUseCase;

    @Mock
    private UpdateTemplateUseCase updateTemplateUseCase;

    @Mock
    private DeleteTemplateUseCase deleteTemplateUseCase;

    @Mock
    private CreateMessageUseCase createMessageUseCase;

    @InjectMocks
    private Handler handler;

    @BeforeAll
    public void init() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(request.bodyToMono(Template.class))
                .thenReturn(Mono.just(SampleData.template()));
        Mockito.when(request.bodyToMono(TemplateDTO.class))
                .thenReturn(Mono.just(SampleData.templaterDTO()));
        Mockito.when(request.bodyToMono(DeleteTemplateDTO.class))
                .thenReturn(Mono.just(SampleData.deleteTemplaterDTO()));
    }

    @Test
    void getTemplateSuccessful() {
        Mockito.when(getTemplateUseCase.getTemplate(Mockito.any()))
                .thenReturn(Mono.just(SampleData.template()));
        StepVerifier.create(handler.getTemplate(SampleData.getRequest()))
                .assertNext(serverResponse ->
                        Assertions.assertThat(serverResponse).isInstanceOf(ServerResponse.class))
                .verifyComplete();
    }

    @Test
    void getTemplateUnsuccessfulBusiness() {
        Mockito.when(getTemplateUseCase.getTemplate(Mockito.any()))
                .thenReturn(Mono.error(new BusinessException(BusinessExceptionEnum.TEMPLATE_NOT_FOUND)));
        StepVerifier.create(handler.getTemplate(SampleData.getRequest()))
                .assertNext(serverResponse -> {
                    Assertions.assertThat(serverResponse).isInstanceOf(ServerResponse.class);
                    Assertions.assertThat(serverResponse.statusCode()).isEqualTo(HttpStatus.CONFLICT);
                }).verifyComplete();
    }

    @Test
    void getTemplateUnsuccessfulTechnical() {
        Mockito.when(getTemplateUseCase.getTemplate(Mockito.any()))
                .thenReturn(Mono.error(new TechnicalException(TechnicalExceptionEnum.MISSING_PARAMETER)));
        StepVerifier.create(handler.getTemplate(SampleData.getRequest()))
                .assertNext(serverResponse -> {
                    Assertions.assertThat(serverResponse).isInstanceOf(ServerResponse.class);
                    Assertions.assertThat(serverResponse.statusCode()).isEqualTo(HttpStatus.CONFLICT);
                }).verifyComplete();
    }

    @Test
    void createTemplateSuccess() {
        Mockito.when(createTemplateUseCase.createTemplate(Mockito.any(Template.class)))
                .thenReturn(Mono.just(SampleData.template()));
        StepVerifier.create(handler.createTemplate(request))
                .assertNext(res -> {
                    Assertions.assertThat(res).isInstanceOf(ServerResponse.class);
                    Assertions.assertThat(res.statusCode()).isEqualTo(HttpStatus.OK);
                }).verifyComplete();
    }

    @Test
    void createTemplateUnsuccessfulTechnical() {
        Mockito.when(createTemplateUseCase.createTemplate(Mockito.any(Template.class)))
                .thenReturn(Mono.error(new TechnicalException(TechnicalExceptionEnum.MISSING_PARAMETER)));
        StepVerifier.create(handler.createTemplate(request))
                .assertNext(res -> {
                    Assertions.assertThat(res).isInstanceOf(ServerResponse.class);
                    Assertions.assertThat(res.statusCode()).isEqualTo(HttpStatus.CONFLICT);
                }).verifyComplete();
    }

    @Test
    void createTemplateUnsuccessfulBusiness() {
        Mockito.when(createTemplateUseCase.createTemplate(Mockito.any(Template.class)))
                .thenReturn(Mono.error(new BusinessException(BusinessExceptionEnum.TEMPLATE_ALREADY_EXISTS)));
        StepVerifier.create(handler.createTemplate(request))
                .assertNext(res -> {
                    Assertions.assertThat(res)
                            .isInstanceOf(ServerResponse.class);
                    Assertions.assertThat(res.statusCode()).isEqualTo(HttpStatus.CONFLICT);
                }).verifyComplete();
    }

    @Test
    void updateTemplateSuccess() {
        Mockito.when(updateTemplateUseCase.updateTemplate(Mockito.any(Template.class)))
                .thenReturn(Mono.just(SampleData.updateTemplateResponse()));
        StepVerifier.create(handler.updateTemplate(request))
                .assertNext(serverResponse ->
                        Assertions.assertThat(serverResponse).isInstanceOf(ServerResponse.class)
                ).verifyComplete();
    }

    @Test
    void updateTemplateUnsuccessfulBusinessTest() {
        Mockito.when(updateTemplateUseCase.updateTemplate(Mockito.any(Template.class)))
                .thenReturn(Mono.error(new BusinessException(BusinessExceptionEnum.TEMPLATE_NOT_FOUND)));
        StepVerifier.create(handler.updateTemplate(request))
                .assertNext(res -> {
                    Assertions.assertThat(res).isInstanceOf(ServerResponse.class);
                    Assertions.assertThat(res.statusCode()).isEqualTo(HttpStatus.CONFLICT);
                }).verifyComplete();
    }

    @Test
    void updateTemplateUnsuccessfulTechnicalTest() {
        Mockito.when(updateTemplateUseCase.updateTemplate(Mockito.any(Template.class)))
                .thenReturn(Mono.error(new TechnicalException(TechnicalExceptionEnum.MISSING_PARAMETER)));
        StepVerifier.create(handler.updateTemplate(request))
                .assertNext(serverResponse -> {
                    Assertions.assertThat(serverResponse).isInstanceOf(ServerResponse.class);
                    Assertions.assertThat(serverResponse.statusCode()).isEqualTo(HttpStatus.CONFLICT);
                }).verifyComplete();
    }

    @Test
    void deleteTemplateSuccess() {
        Mockito.when(deleteTemplateUseCase.deleteTemplate(Mockito.any(Template.class)))
                .thenReturn(Mono.just(SampleData.deleteTemplateResponse()));
        StepVerifier.create(handler.deleteTemplate(request))
                .assertNext(serverResponse ->
                        Assertions.assertThat(serverResponse).isInstanceOf(ServerResponse.class)
                ).verifyComplete();
    }

    @Test
    void deleteTemplateUnsuccessfulBusinessTest() {
        Mockito.when(deleteTemplateUseCase.deleteTemplate(Mockito.any(Template.class)))
                .thenReturn(Mono.error(new BusinessException(BusinessExceptionEnum.TEMPLATE_NOT_FOUND)));
        StepVerifier.create(handler.deleteTemplate(request))
                .assertNext(res -> {
                    Assertions.assertThat(res).isInstanceOf(ServerResponse.class);
                    Assertions.assertThat(res.statusCode()).isEqualTo(HttpStatus.CONFLICT);
                }).verifyComplete();
    }

    @Test
    void deleteTemplateUnsuccessfulTechnicalTest() {
        Mockito.when(deleteTemplateUseCase.deleteTemplate(Mockito.any(Template.class)))
                .thenReturn(Mono.error(new TechnicalException(TechnicalExceptionEnum.MISSING_PARAMETER)));
        StepVerifier.create(handler.deleteTemplate(request))
                .assertNext(serverResponse -> {
                    Assertions.assertThat(serverResponse).isInstanceOf(ServerResponse.class);
                    Assertions.assertThat(serverResponse.statusCode()).isEqualTo(HttpStatus.CONFLICT);
                }).verifyComplete();
    }

    @Test
    void createMessageSuccessful() {
        Mockito.when(createMessageUseCase.createMessage(Mockito.any()))
                .thenReturn(Mono.just(SampleData.messageResponse()));
        StepVerifier.create(handler.createMessage(SampleData.createMessageRequest()))
                .assertNext(serverResponse ->
                        Assertions.assertThat(serverResponse).isInstanceOf(ServerResponse.class))
                .verifyComplete();
    }

    @Test
    void createMessageUnsuccessfulBusiness() {
        Mockito.when(createMessageUseCase.createMessage(Mockito.any()))
                .thenReturn(Mono.error(new BusinessException(BusinessExceptionEnum.TEMPLATE_NOT_FOUND)));
        StepVerifier.create(handler.createMessage(SampleData.createMessageRequest()))
                .assertNext(serverResponse -> {
                    Assertions.assertThat(serverResponse).isInstanceOf(ServerResponse.class);
                    Assertions.assertThat(serverResponse.statusCode()).isEqualTo(HttpStatus.CONFLICT);
                }).verifyComplete();
    }

    @Test
    void createMessageUnsuccessfulTechnical() {
        Mockito.when(createMessageUseCase.createMessage(Mockito.any()))
                .thenReturn(Mono.error(new TechnicalException(TechnicalExceptionEnum.MISSING_PARAMETER)));
        StepVerifier.create(handler.createMessage(SampleData.createMessageRequest()))
                .assertNext(serverResponse -> {
                    Assertions.assertThat(serverResponse).isInstanceOf(ServerResponse.class);
                    Assertions.assertThat(serverResponse.statusCode()).isEqualTo(HttpStatus.CONFLICT);
                }).verifyComplete();
    }

    @Test
    void validatorTest() {
        validator.validateBody(SampleData.templaterDTO());
    }
}
