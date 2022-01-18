package co.com.bancolombia.api;

import co.com.bancolombia.api.commons.RequestValidator;
import co.com.bancolombia.api.dto.TemplaterDTO;
import co.com.bancolombia.commons.enums.BusinessExceptionEnum;
import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.template.dto.TemplateRequest;
import co.com.bancolombia.model.template.dto.TemplateResponse;
import co.com.bancolombia.usecase.createtemplate.CreateTemplateUseCase;
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
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private GenericBaseHandler genericBaseHandler;

    @InjectMocks
    private Handler handler;

    @BeforeAll
    public void init() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(request.bodyToMono(TemplateRequest.class))
                .thenReturn(Mono.just(SampleData.templaterRequest()));
        Mockito.when(request.bodyToMono(TemplaterDTO.class))
                .thenReturn(Mono.just(SampleData.templaterDTO()));
    }

    @Test
    void headersTemplate() {
        Map<String, String> headers = new HashMap<>();
        headers.put("id", "01");
        headers.put("messageType", "Type");
        MockServerRequest request = MockServerRequest.builder()
                .header("id", "01")
                .header("messageType", "Type")
                .build();
        Mockito.when(genericBaseHandler.setHeaders(request))
                .thenReturn(headers);
        StepVerifier.create(handler.templaterDTOMono(request))
                .assertNext(templaterDTO -> Assertions.assertThat(templaterDTO).isInstanceOf(TemplaterDTO.class))
                .verifyComplete();
    }

    @Test
    void getTemplateSuccessful() {
        Map<String, String> headers = new HashMap<>();
        headers.put("id", "01");
        headers.put("messageType", "Type");
        MockServerRequest request = MockServerRequest.builder()
                .header("id", "01")
                .header("messageType", "Type")
                .build();
        List<TemplateResponse> templateResponses = new ArrayList<>();
        templateResponses.add(SampleData.templateResponse());
        Mockito.when(getTemplateUseCase.getTemplate(headers))
                .thenReturn(Mono.just(templateResponses));
        StepVerifier.create(handler.getTemplate(request))
                .assertNext(serverResponse ->
                        Assertions.assertThat(serverResponse).isInstanceOf(ServerResponse.class))
                .verifyComplete();
    }

    @Test
    void getTemplateUnsuccessfulBusiness() {
        Map<String, String> headers = new HashMap<>();
        headers.put("id", "01");
        headers.put("messageType", "Type");
        MockServerRequest request = MockServerRequest.builder()
                .header("id", "01")
                .header("messageType", "Type")
                .build();
        Mockito.when(getTemplateUseCase.getTemplate(headers))
                .thenReturn(Mono.error(new BusinessException(BusinessExceptionEnum.TEMPLATE_NOT_FOUND)));
        StepVerifier.create(handler.getTemplate(request))
                .assertNext(serverResponse -> {
                    Assertions.assertThat(serverResponse).isInstanceOf(ServerResponse.class);
                    Assertions.assertThat(serverResponse.statusCode()).isEqualTo(HttpStatus.CONFLICT);
                }).verifyComplete();
    }

    @Test
    void getTemplateUnsuccessfulTechnical() {
        Map<String, String> headers = new HashMap<>();
        headers.put("id", "01");
        headers.put("messageType", "Type");
        MockServerRequest request = MockServerRequest.builder()
                .header("id", "01")
                .header("messageType", "Type")
                .build();
        Mockito.when(getTemplateUseCase.getTemplate(headers))
                .thenReturn(Mono.error(new TechnicalException(TechnicalExceptionEnum.MISSING_PARAMETER)));
        StepVerifier.create(handler.getTemplate(request))
                .assertNext(serverResponse -> {
                    Assertions.assertThat(serverResponse).isInstanceOf(ServerResponse.class);
                    Assertions.assertThat(serverResponse.statusCode()).isEqualTo(HttpStatus.CONFLICT);
                }).verifyComplete();
    }

    @Test
    void createTemplateSuccess() {
        Mockito.when(createTemplateUseCase.createTemplate(Mockito.any(TemplateRequest.class)))
                .thenReturn(Mono.just(SampleData.templateResponse()));
        StepVerifier.create(handler.createTemplate(request))
                .assertNext(res -> {
                    Assertions.assertThat(res).isInstanceOf(ServerResponse.class);
                    Assertions.assertThat(res.statusCode()).isEqualTo(HttpStatus.OK);
                }).verifyComplete();
    }

    @Test
    void createTemplateUnsuccessfulTechnical() {
        Mockito.when(createTemplateUseCase.createTemplate(Mockito.any(TemplateRequest.class)))
                .thenReturn(Mono.error(new TechnicalException(TechnicalExceptionEnum.MISSING_PARAMETER)));
        StepVerifier.create(handler.createTemplate(request))
                .assertNext(res -> {
                    Assertions.assertThat(res).isInstanceOf(ServerResponse.class);
                    Assertions.assertThat(res.statusCode()).isEqualTo(HttpStatus.CONFLICT);
                }).verifyComplete();
    }

    @Test
    void createTemplateUnsuccessfulBusiness() {
        Mockito.when(createTemplateUseCase.createTemplate(Mockito.any(TemplateRequest.class)))
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
        Mockito.when(updateTemplateUseCase.updateTemplate(Mockito.any(TemplateRequest.class)))
                .thenReturn(Mono.just(Map.of("actual", TemplateResponse.builder().build())));
        StepVerifier.create(handler.updateTemplate(request))
                .assertNext(serverResponse ->
                        Assertions.assertThat(serverResponse).isInstanceOf(ServerResponse.class)
                ).verifyComplete();
    }

    @Test
    void updateTemplateUnsuccessfulBusinessTest() {
        Mockito.when(updateTemplateUseCase.updateTemplate(Mockito.any(TemplateRequest.class)))
                .thenReturn(Mono.error(new BusinessException(BusinessExceptionEnum.TEMPLATE_NOT_FOUND)));
        StepVerifier.create(handler.updateTemplate(request))
                .assertNext(res -> {
                    Assertions.assertThat(res).isInstanceOf(ServerResponse.class);
                    Assertions.assertThat(res.statusCode()).isEqualTo(HttpStatus.CONFLICT);
                }).verifyComplete();
    }

    @Test
    void updateTemplateUnsuccessfulTechnicalTest() {
        Mockito.when(updateTemplateUseCase.updateTemplate(Mockito.any(TemplateRequest.class)))
                .thenReturn(Mono.error(new TechnicalException(TechnicalExceptionEnum.MISSING_PARAMETER)));
        StepVerifier.create(handler.updateTemplate(request))
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
