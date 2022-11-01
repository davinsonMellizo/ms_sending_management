package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.DeleteTemplateDTO;
import co.com.bancolombia.api.dto.TemplateDTO;
import co.com.bancolombia.model.template.dto.MessageResponse;
import co.com.bancolombia.model.template.dto.Template;
import co.com.bancolombia.model.template.dto.UpdateTemplateResponse;
import org.json.JSONObject;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class SampleData {

    public static Template template() {
        return Template.builder()
                .idTemplate("01")
                .messageType("Type")
                .description("Firts template")
                .messageSubject("Subject")
                .messageBody("Body")
                .plainText("Text")
                .creationUser("User")
                .idConsumer("ConsumerID")
                .build();
    }

    public static TemplateDTO templaterDTO() {
        return TemplateDTO.builder()
                .idTemplate("01")
                .messageType("Type")
                .description("Firts template")
                .messageSubject("Subject")
                .messageBody("Body")
                .plainText("Text")
                .user("User")
                .idConsumer("ConsumerID")
                .status("1")
                .build();
    }

    public static DeleteTemplateDTO deleteTemplaterDTO() {
        return DeleteTemplateDTO.builder()
                .idTemplate("01")
                .build();
    }

    public static UpdateTemplateResponse updateTemplateResponse() {
        return UpdateTemplateResponse.builder()
                .before(template())
                .current(template())
                .build();
    }

    public static UpdateTemplateResponse deleteTemplateResponse() {
        return UpdateTemplateResponse.builder()
                .before(template())
                .current(template().toBuilder().status("0").build())
                .build();
    }

    public static MockServerRequest getRequest() {
        return MockServerRequest.builder()
                .queryParam("idTemplate", "001")
                .build();
    }

    public static MessageResponse messageResponse() {
        return MessageResponse.builder()
                .idTemplate("001")
                .messageSubject("Subject")
                .messageBody("Body")
                .plainText("Plain text")
                .build();
    }

    public static MockServerRequest createMessageRequest() {
        Map<String, String> data = new HashMap<>();
        data.put("name", "test");
        JSONObject obj = new JSONObject();
        return MockServerRequest.builder()
                .queryParam("idTemplate", "001")
                .body(Mono.just(data));
    }

}
