package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.TemplaterDTO;
import co.com.bancolombia.model.template.dto.TemplateRequest;
import co.com.bancolombia.model.template.dto.TemplateResponse;

public class SampleData {

    public static TemplateRequest templaterRequest() {
        return TemplateRequest.builder()
                .id("01")
                .messageType("Type")
                .messageSubject("Subject")
                .messageBody("Body")
                .messageText("Text")
                .creationUser("User")
                .consumerId("ConsumerID")
                .build();
    }

    public static TemplaterDTO templaterDTO() {
        return TemplaterDTO.builder()
                .id("01")
                .messageType("Type")
                .messageSubject("Subject")
                .messageBody("Body")
                .messageText("Text")
                .creationUser("User")
                .consumerId("ConsumerID")
                .build();
    }

    public static TemplateResponse templateResponse() {
        return TemplateResponse.builder()
                .id("01")
                .messageType("Type")
                .messageSubject("Subject")
                .messageBody("Body")
                .messageText("Text")
                .consumerId("ConsumerID")
                .build();
    }


}
