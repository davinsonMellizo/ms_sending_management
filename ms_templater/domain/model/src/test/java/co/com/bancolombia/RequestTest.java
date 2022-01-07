package co.com.bancolombia;

import co.com.bancolombia.commons.constants.Constants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RequestTest {

    @InjectMocks
    private Request request;

    @BeforeAll
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void requestTest() {
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.ID, "Test-ID-123");
        headers.put(Constants.MESSAGE_TYPE, "EMAIL");
        headers.put(Constants.MESSAGE_SUBJECT, "Email Subject");
        assertThat(request.headers(headers)).isNotNull();
    }

    @Test
    void requestExistingHeaders() {
        Map<String, String> currentHeaders = new HashMap<>();
        currentHeaders.put("Content-Type", "application/json");
        ReflectionTestUtils.setField(request, "headers", currentHeaders);
        Map<String, String> newHeaders = new HashMap<>();
        newHeaders.put(Constants.ID, "Test-ID-123");
        newHeaders.put(Constants.MESSAGE_TYPE, "EMAIL");
        newHeaders.put(Constants.MESSAGE_SUBJECT, "Email Subject");
        assertThat(request.headers(newHeaders)).isNotNull();
    }

    @Test
    void blankHeadersTest() {
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.ID, "Test-ID-123");
        headers.put(Constants.MESSAGE_TYPE, "EMAIL");
        headers.put(Constants.MESSAGE_SUBJECT, "");
        assertThat(request.headers(headers)).isNotNull();
    }
}
