package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.DeleteTemplaterDTO;
import co.com.bancolombia.api.dto.TemplaterDTO;
import co.com.bancolombia.model.template.dto.TemplateRequest;
import co.com.bancolombia.model.template.dto.TemplateResponse;

public class SampleData {

    public static TemplateRequest templaterRequest() {
        return TemplateRequest.builder()
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

    public static TemplaterDTO templaterDTO() {
        return TemplaterDTO.builder()
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

    public static DeleteTemplaterDTO deleteTemplaterDTO() {
        return DeleteTemplaterDTO.builder()
                .idTemplate("01")
                .build();
    }

    public static TemplateResponse templateResponse() {
        return TemplateResponse.builder()
                .idTemplate("01")
                .messageType("Type")
                .messageSubject("Subject")
                .messageBody("Body")
                .plainText("Text")
                .idConsumer("ConsumerID")
                .build();
    }


}
