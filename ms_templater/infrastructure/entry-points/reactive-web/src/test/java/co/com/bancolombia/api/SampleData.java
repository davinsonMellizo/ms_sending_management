package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.DeleteTemplaterDTO;
import co.com.bancolombia.api.dto.TemplaterDTO;
import co.com.bancolombia.model.template.dto.Template;
import co.com.bancolombia.model.template.dto.UpdateTemplateResponse;

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

    public static UpdateTemplateResponse updateTemplateResponse() {
        return UpdateTemplateResponse.builder()
                .before(template())
                .current(template())
                .build();
    }


}
