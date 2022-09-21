package co.com.bancolombia.model.template.dto;

import co.com.bancolombia.commons.constants.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TemplateTest {

    @Test
    void templateRequestTest() {
        Template template = Template.builder().idTemplate("0001").messageType("Message Type").version("Version")
                .idConsumer("ID Consumer").description("Description").messageSubject("Message Subject")
                .messageBody("Message Body").plainText("Message").creationUser("Creation User")
                .creationDate("Creation Date").modificationUser("Modification User")
                .modificationDate("Modification Date").build();
        assertThat(template).isNotEqualTo(Template.builder().build());
        assertThat(template.hashCode()).isGreaterThan(Constants.PRIME);
    }
}
