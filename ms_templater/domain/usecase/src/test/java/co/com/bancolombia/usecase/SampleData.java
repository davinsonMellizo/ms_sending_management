package co.com.bancolombia.usecase;

import co.com.bancolombia.model.template.dto.TemplateRequest;
import co.com.bancolombia.model.template.dto.TemplateResponse;

import java.util.HashMap;
import java.util.Map;

public class SampleData {

    public static TemplateRequest templateRequest() {
        return TemplateRequest.builder()
                .MessageType("EMAIL")
                .Description("First Template")
                .MessageSubject("Subject")
                .MessageBody("Body")
                .PlainText("Message")
                .CreationUser("User ID")
                .IdConsumer("Consumer ID")
                .build();
    }

    public static TemplateResponse templateResponse() {
        return TemplateResponse.builder()
                .IdTemplate("001")
                .MessageType("EMAIL")
                .Description("First Template")
                .MessageSubject("Subject")
                .MessageBody("Body")
                .PlainText("Message")
                .IdConsumer("Consumer ID")
                .build();
    }

    public static TemplateRequest templateRequestUpdate() {
        return TemplateRequest.builder()
                .IdTemplate("001")
                .MessageType("EMAIL")
                .Description("First Template")
                .MessageBody("Subject")
                .MessageBody("New Body")
                .PlainText("Message")
                .CreationUser("User ID")
                .IdConsumer("Consumer ID")
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
