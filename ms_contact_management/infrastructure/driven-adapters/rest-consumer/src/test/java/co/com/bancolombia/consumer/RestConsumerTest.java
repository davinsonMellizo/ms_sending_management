package co.com.bancolombia.consumer;

import co.com.bancolombia.consumer.adapter.Error;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.log.LoggerBuilder;
import co.com.bancolombia.model.Request;
import co.com.bancolombia.model.client.ResponseClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.HashMap;

import static co.com.bancolombia.consumer.Commons.getBaseUrl;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        ConsumerProperties.class
})
public class RestConsumerTest {
    private MockWebServer mockServer;
    private RestConsumer restClient;
    private final ResponseClient client = new ResponseClient();
    @Mock
    private LoggerBuilder loggerBuilder;

    @Autowired
    private ConsumerProperties properties;

    @BeforeEach
    public void setUp() {
        client.setResponse(true);
        mockServer = new MockWebServer();
        restClient = new RestConsumer(WebClient.builder()
                .baseUrl(getBaseUrl(mockServer.getPort()))
                .build(), loggerBuilder);
        mockServer.url(getBaseUrl(mockServer.getPort()));
    }

    @AfterEach
    public void tearDown() throws IOException {
        mockServer.shutdown();
    }

    @Test
    public void post(){
        mockServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .addHeader(CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .setBody("{\"response\": true}"));

        HashMap headers = new HashMap();
        Request request = Request.builder().headers(headers).build();
        StepVerifier.create(restClient.post(getBaseUrl(mockServer.getPort()), request,
                ResponseClient.class, Error.class))
                .expectNextMatches(response -> ((ResponseClient) response).getResponse()==true)
                .verifyComplete();
    }

}
