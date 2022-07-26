package co.com.bancolombia.usecase;

import co.com.bancolombia.model.template.dto.MessageRequest;
import co.com.bancolombia.model.template.dto.Template;

import java.util.HashMap;
import java.util.Map;

public class SampleData {

    public static Template template() {
        return Template.builder()
                .idTemplate("001")
                .messageType("EMAIL")
                .description("First Template")
                .messageSubject("Subject")
                .messageBody("Body")
                .plainText("Message")
                .creationUser("User ID_TEMPLATE")
                .idConsumer("Consumer ID_TEMPLATE")
                .status("1")
                .build();
    }

    public static Template templateRequestUpdate() {
        return Template.builder()
                .idTemplate("001")
                .messageType("EMAIL")
                .description("First Template")
                .messageBody("Subject")
                .messageBody("New Body")
                .plainText("Message")
                .creationUser("User ID_TEMPLATE")
                .idConsumer("Consumer ID_TEMPLATE")
                .build();
    }

    public static Template templateRequestDelete() {
        return Template.builder()
                .idTemplate("001")
                .build();
    }

    public static MessageRequest messageRequest() {
        Map<String, String> data = new HashMap<>();
        data.put("name", "test");
        data.put("balance", "test");
        return MessageRequest.builder()
                .idTemplate("001")
                .messageValues(data)
                .build();
    }

    public static Template getRequest() {
        return Template.builder()
                .idTemplate("001")
                .build();
    }
}
