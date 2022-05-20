package co.com.bancolombia.commons.freemarker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DeliveryTemplateTest {

    private static final String templateTest = "<#assign headers = data.headers>${BusinessUtil.getValue(headers,'message-id')}";

    @Test
    void createTest() {
        assertThat(CommonTemplate.create(templateTest)).isNotNull();
    }

    @Test
    void converFuncion() {
        Map<String, Object> data = new HashMap<>();
        data.put("data", Map.of("headers", Map.of("message-id", "123456789")));
        CommonTemplate.create(templateTest).flatMap(tem -> tem.process(data)).block();
        StepVerifier.create(CommonTemplate.create(templateTest).flatMap(tem -> tem.process(data)))
                .expectNext("123456789")
                .verifyComplete();
        assertThat(CommonTemplate.create(templateTest)).isNotNull();
    }
}
