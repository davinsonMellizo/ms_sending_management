package co.com.bancolombia.api;

import co.com.bancolombia.api.commons.RequestValidator;
import co.com.bancolombia.api.dto.DeleteTemplaterDTO;
import co.com.bancolombia.api.dto.TemplaterDTO;
import co.com.bancolombia.commons.constants.Constants;
import co.com.bancolombia.commons.enums.BusinessExceptionEnum;
import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.template.dto.Template;
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
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
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
    private DeleteTemplateUseCase deleteTemplateUseCase;

    @Mock
    private GenericBaseHandler genericBaseHandler;

    @InjectMocks
    private Handler handler;

    @BeforeAll
    public void init() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(request.bodyToMono(Template.class))
                .thenReturn(Mono.just(SampleData.template()));
        Mockito.when(request.bodyToMono(TemplaterDTO.class))
                .thenReturn(Mono.just(SampleData.templaterDTO()));
        Mockito.when(request.bodyToMono(DeleteTemplaterDTO.class))
                .thenReturn(Mono.just(SampleData.deleteTemplaterDTO()));
    }

    @Test
    void headersTemplate() {
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.ID_TEMPLATE, "01");
        headers.put(Constants.MESSAGE_TYPE, "Type");
        MockServerRequest request = MockServerRequest.builder()
                .header(Constants.ID_TEMPLATE, "01")
                .header(Constants.MESSAGE_TYPE, "Type")
                .build();
        Mockito.when(genericBaseHandler.setHeaders(request))
                .thenReturn(headers);
        StepVerifier.create(handler.templaterDTOMono(request))
                .assertNext(templaterDTO -> Assertions.assertThat(templaterDTO).isInstanceOf(Map.class))
                .verifyComplete();
    }

    @Test
    void getTemplateSuccessful() {
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.ID_TEMPLATE, "01");
        headers.put(Constants.MESSAGE_TYPE, "Type");
        MockServerRequest request = MockServerRequest.builder()
                .header(Constants.ID_TEMPLATE, "01")
                .header(Constants.MESSAGE_TYPE, "Type")
                .build();
        Mockito.when(getTemplateUseCase.getTemplate(headers))
                .thenReturn(Mono.just(SampleData.template()));
        StepVerifier.create(handler.getTemplate(request))
                .assertNext(serverResponse ->
                        Assertions.assertThat(serverResponse).isInstanceOf(ServerResponse.class))
                .verifyComplete();
    }

    @Test
    void getTemplateUnsuccessfulBusiness() {
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.ID_TEMPLATE, "01");
        headers.put(Constants.MESSAGE_TYPE, "Type");
        MockServerRequest request = MockServerRequest.builder()
                .header(Constants.ID_TEMPLATE, "01")
                .header(Constants.MESSAGE_TYPE, "Type")
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
        headers.put(Constants.ID_TEMPLATE, "01");
        headers.put(Constants.MESSAGE_TYPE, "Type");
        MockServerRequest request = MockServerRequest.builder()
                .header(Constants.ID_TEMPLATE, "01")
                .header(Constants.MESSAGE_TYPE, "Type")
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
//
//    @Test
//    void deleteTemplateSuccess() {
//        Mockito.when(deleteTemplateUseCase.deleteTemplate(Mockito.any(Template.class)))
//                .thenReturn(Mono.just(TemplateResponse.builder().build()));
//        StepVerifier.create(handler.deleteTemplate(request))
//                .assertNext(serverResponse ->
//                        Assertions.assertThat(serverResponse).isInstanceOf(ServerResponse.class)
//                ).verifyComplete();
//    }
//
//    @Test
//    void deleteTemplateUnsuccessfulBusinessTest() {
//        Mockito.when(deleteTemplateUseCase.deleteTemplate(Mockito.any(Template.class)))
//                .thenReturn(Mono.error(new BusinessException(BusinessExceptionEnum.TEMPLATE_NOT_FOUND)));
//        StepVerifier.create(handler.deleteTemplate(request))
//                .assertNext(res -> {
//                    Assertions.assertThat(res).isInstanceOf(ServerResponse.class);
//                    Assertions.assertThat(res.statusCode()).isEqualTo(HttpStatus.CONFLICT);
//                }).verifyComplete();
//    }
//
//    @Test
//    void deleteTemplateUnsuccessfulTechnicalTest() {
//        Mockito.when(deleteTemplateUseCase.deleteTemplate(Mockito.any(Template.class)))
//                .thenReturn(Mono.error(new TechnicalException(TechnicalExceptionEnum.MISSING_PARAMETER)));
//        StepVerifier.create(handler.deleteTemplate(request))
//                .assertNext(serverResponse -> {
//                    Assertions.assertThat(serverResponse).isInstanceOf(ServerResponse.class);
//                    Assertions.assertThat(serverResponse.statusCode()).isEqualTo(HttpStatus.CONFLICT);
//                }).verifyComplete();
//    }

    @Test
    void validatorTest() {
        validator.validateBody(SampleData.templaterDTO());
    }
}
