package co.com.bancolombia.usecase;

import co.com.bancolombia.commons.constants.Constants;
import co.com.bancolombia.model.template.dto.TemplateRequest;
import co.com.bancolombia.model.template.dto.TemplateResponse;

import java.util.HashMap;
import java.util.Map;

public class SampleData {

    public static TemplateRequest templateRequest() {
        return TemplateRequest.builder()
                .idTemplate("001")
                .messageType("EMAIL")
                .description("First Template")
                .messageSubject("Subject")
                .messageBody("Body")
                .plainText("Message")
                .creationUser("User ID_TEMPLATE")
                .idConsumer("Consumer ID_TEMPLATE")
                .build();
    }

    public static TemplateResponse templateResponse() {
        return TemplateResponse.builder()
                .idTemplate("001")
                .messageType("EMAIL")
                .description("First Template")
                .messageSubject("Subject")
                .messageBody("Body")
                .plainText("Message")
                .idConsumer("Consumer ID_TEMPLATE")
                .build();
    }

    public static TemplateRequest templateRequestUpdate() {
        return TemplateRequest.builder()
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
    public static TemplateRequest templateRequestDelete() {
        return TemplateRequest.builder()
                .idTemplate("001")
                .build();
    }

    public static Map<String, String> testHeader() {
        Map<String, String> testHeader = new HashMap<>();
        testHeader.put(Constants.ID_TEMPLATE, "001");
        testHeader.put(Constants.MESSAGE_TYPE, "Test");
        testHeader.put(Constants.MESSAGE_SUBJECT, "Test");
        return testHeader;
    }
}
