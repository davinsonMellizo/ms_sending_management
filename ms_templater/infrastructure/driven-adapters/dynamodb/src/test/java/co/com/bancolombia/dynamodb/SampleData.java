package co.com.bancolombia.dynamodb;

import co.com.bancolombia.commons.constants.Constants;
import co.com.bancolombia.dynamodb.data.Templater;
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
                .build();
    }

    public static Templater templater() {
        Templater templater = new Templater();
        templater.setIdTemplate("001");
        templater.setMessageType("EMAIL");
        templater.setDescription("First Template");
        templater.setMessageSubject("Subject");
        templater.setMessageBody("Body");
        templater.setPlainText("Message");
        templater.setCreationUser("User ID_TEMPLATE");
        templater.setIdConsumer("Consumer ID_TEMPLATE");
        return templater;
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
}
