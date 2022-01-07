package co.com.bancolombia.usecase;

import co.com.bancolombia.model.template.dto.TemplateRequest;
import co.com.bancolombia.model.template.dto.TemplateResponse;

import java.util.HashMap;
import java.util.Map;

public class SampleData {

    public static TemplateRequest templateRequest() {
        return TemplateRequest.builder()
                .messageType("EMAIL")
                .messageSubject("Subject")
                .messageBody("Body")
                .messageText("Message")
                .creationUser("User ID")
                .consumerId("Consumer ID")
                .build();
    }

    public static TemplateResponse templateResponse() {
        return TemplateResponse.builder()
                .id("001")
                .messageType("EMAIL")
                .messageSubject("Subject")
                .messageBody("Body")
                .messageText("Message")
                .consumerId("Consumer ID")
                .build();
    }

    public static TemplateRequest templateRequestUpdate() {
        return TemplateRequest.builder()
                .id("001")
                .messageType("EMAIL")
                .messageSubject("Subject")
                .messageBody("New Body")
                .messageText("Message")
                .creationUser("User ID")
                .consumerId("Consumer ID")
                .build();
    }

    public static Map<String, String> testHeader() {
        Map<String, String> testHeader = new HashMap<>();
        testHeader.put("id", "001");
        testHeader.put("messageType", "Test");
        testHeader.put("messageSubject", "Test");
        return testHeader;
    }
}
