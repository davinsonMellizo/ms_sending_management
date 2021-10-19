package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.consumer.RestConsumer;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.client.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static co.com.bancolombia.consumer.Commons.getBaseUrl;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        ConsumerProperties.class
})
public class ClientAdapterTest {

    private ClientAdapter clientAdapter;
    private RestConsumer restConsumer;
    @Autowired
    private ConsumerProperties properties;

    private final Client client = new Client();

    @BeforeEach
    public void init() {
        client.setDocumentNumber(new Long(1061772353));
        client.setDocumentType("0");
        client.setEnrollmentOrigin("ALM");

        restConsumer = new RestConsumer(WebClient.builder()
                .baseUrl(getBaseUrl(8080))
                .build());
        clientAdapter = new ClientAdapter(properties, restConsumer);
    }

    @Test
    public void matchClientWithBasicKit(){
        StepVerifier.create(clientAdapter.matchClientWithBasicKit(client))
                .expectError();
    }

}
