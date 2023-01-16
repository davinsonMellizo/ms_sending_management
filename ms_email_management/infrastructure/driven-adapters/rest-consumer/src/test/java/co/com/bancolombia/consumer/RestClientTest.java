package co.com.bancolombia.consumer;

import co.com.bancolombia.consumer.adapter.response.ErrorMasivianMAIL;
import co.com.bancolombia.consumer.adapter.response.SuccessMasivianMAIL;
import co.com.bancolombia.model.message.Mail;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.Map;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON;

class RestClientTest {
    private MockWebServer mockServer;
    private RestClient restClient;
    public static final String HOST = "http://localhost:%s/";
    private Mail mail;

    @BeforeEach
    public void init() {
        mail = new Mail();
        mockServer = new MockWebServer();
        restClient = new RestClient(WebClient.builder()
                .baseUrl(getBaseUrl(mockServer.getPort()))
                .build());
        mockServer.url(getBaseUrl(mockServer.getPort()));

        mail.setHeaders(Map.of("Authorization", "TokenTest"));

    }

    public static String getBaseUrl(int port) {
        return String.format(HOST, port);
    }

    @AfterEach
    public void tearDown() throws IOException {
        mockServer.shutdown();
    }

    @Test
    void givenPostThenSuccess() {
        mockServer.enqueue(mockResponseSuccess());
        StepVerifier.create(restClient.post(getBaseUrl(mockServer.getPort()), mail, SuccessMasivianMAIL.class,
                ErrorMasivianMAIL.class))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void givenPostThenError() {
        mockServer.enqueue(mockResponseError());
        StepVerifier.create(restClient.post(getBaseUrl(mockServer.getPort()), mail, SuccessMasivianMAIL.class,
                ErrorMasivianMAIL.class))
                .expectError()
                .verify();
    }

    public static MockResponse mockResponseSuccess() {
        return new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .setBody("{}");
    }

    public static MockResponse mockResponseError() {
        return new MockResponse()
                .setResponseCode(HttpStatus.BAD_REQUEST.value())
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .setBody("{}");
    }

}
