package co.com.bancolombia.model.template.dto;

import co.com.bancolombia.commons.constants.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TemplateRequestTest {

    @Test
    void templateRequestTest() {
        TemplateRequest templateRequest = TemplateRequest.builder()
                .messageType("Message Type")
                .messageSubject("Message Subject")
                .messageBody("Message Body")
                .messageText("Message")
                .creationUser("Creation User")
                .build();
        assertThat(templateRequest)
                .isNotEqualTo(TemplateRequest.builder().build())
                .isNotEqualTo(TemplateResponse.builder().build());
        assertThat(templateRequest.hashCode())
                .isNotEqualTo(Constants.PRIME);
    }

    @Test
    void emptyTemplateRequestTest() {
        TemplateRequest templateRequest = TemplateRequest.builder().build();
        assertThat(templateRequest.hashCode())
                .isEqualTo(Constants.PRIME);
    }
}
