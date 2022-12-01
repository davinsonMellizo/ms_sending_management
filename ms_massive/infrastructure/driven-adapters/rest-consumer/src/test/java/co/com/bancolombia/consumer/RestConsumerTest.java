package co.com.bancolombia.consumer;
import co.com.bancolombia.consumer.adapter.campaign.model.ErrorCampaign;
import co.com.bancolombia.consumer.adapter.campaign.model.SuccessCampaign;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import java.io.IOException;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON;

@ExtendWith(MockitoExtension.class)
class RestConsumerTest {
    private MockWebServer mockServer;
    private RestConsumer restClient;
    private MultiValueMap<String, String> queryParams;
    public static final String HOST = "http://localhost:%s/";
    @BeforeEach
    public void init() {
        mockServer = new MockWebServer();
        restClient = new RestConsumer(WebClient.builder()
                .baseUrl(getBaseUrl(mockServer.getPort()))
                .build());
        mockServer.url(getBaseUrl(mockServer.getPort()));
        queryParams = new LinkedMultiValueMap<>();
        queryParams.add("id-campaign", "1");
        queryParams.add("id-consumer", "SVP");
    }
    public static String getBaseUrl(int port) {
        return String.format(HOST, port);
    }
    @AfterEach
    public void tearDown() throws IOException {
        mockServer.shutdown();
    }
    @Test
    void givenGetThenSuccess() {
        mockServer.enqueue(mockResponseSuccess());
        StepVerifier.create(restClient.get(getBaseUrl(mockServer.getPort()), queryParams, SuccessCampaign.class, ErrorCampaign.class))
                .expectNextCount(1)
                .verifyComplete();
    }
    @Test
    void givenGetThenError() {
        mockServer.enqueue(mockResponseError());
        StepVerifier.create(restClient.get(getBaseUrl(mockServer.getPort()), queryParams, SuccessCampaign.class, ErrorCampaign.class))
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