package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.TemplaterDTO;
import co.com.bancolombia.model.template.dto.TemplateRequest;
import co.com.bancolombia.model.template.dto.TemplateResponse;

public class SampleData {

    public static TemplateRequest templaterRequest() {
        return TemplateRequest.builder()
                .IdTemplate("01")
                .MessageType("Type")
                .Description("Firts template")
                .MessageSubject("Subject")
                .MessageBody("Body")
                .PlainText("Text")
                .CreationUser("User")
                .IdConsumer("ConsumerID")
                .build();
    }

    public static TemplaterDTO templaterDTO() {
        return TemplaterDTO.builder()
                .IdTemplate("01")
                .MessageType("Type")
                .Description("Firts template")
                .MessageSubject("Subject")
                .MessageBody("Body")
                .PlainText("Text")
                .CreationUser("User")
                .IdConsumer("ConsumerID")
                .build();
    }

    public static TemplateResponse templateResponse() {
        return TemplateResponse.builder()
                .IdTemplate("01")
                .MessageType("Type")
                .MessageSubject("Subject")
                .MessageBody("Body")
                .PlainText("Text")
                .IdConsumer("ConsumerID")
                .build();
    }


}
