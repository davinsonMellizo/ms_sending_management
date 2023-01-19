package co.com.bancolombia.consumer.adapter;


import co.com.bancolombia.consumer.RestConsumer;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.d2b.model.secret.AsyncSecretVault;
import co.com.bancolombia.model.client.Client;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.UNAUTHORIZED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientAdapterTest {

    @InjectMocks
    private ClientAdapter clientAdapter;

    @Mock
    private RestConsumer<RetrieveRequest, Response> restConsumerIs;
    @Mock
    private AsyncSecretVault secretsManager;
    @Mock
    private ConsumerProperties properties;
    private final ObjectMapper mapper = new ObjectMapper();

    private final Client client = new Client();

    @BeforeEach
    public void init() {
        client.setDocumentType("0");
        client.setEnrollmentOrigin("ALM");
        client.setDocumentNumber(1061772353L);
    }

    @Test
    void retrieveAlertInformationTest() throws JsonProcessingException {
        when(properties.getResources()).thenReturn(new ConsumerProperties.Resources("localhost"));
        client.setDocumentType("CC");
        String data = "{ \"data\": { \"alertIndicators\": [ { \"alertType\": \"ALE\", \"customerMobileNumber\": \"3772056958\", \"customerEmail\": \"CARLOSPOSADA@BANCOLOMBIA.COM.CO\", \"pushActive\": \"0\"} ] } }";
        Response response= mapper.readValue(data, Response.class);
        response.getData().getAlertIndicators().get(0).setLastDataModificationDate(LocalDate.now());
        when(restConsumerIs.post(anyString(), any(), any(), any())).thenReturn(Mono.just(response));
        StepVerifier.create(clientAdapter.retrieveAlertInformation(client))
                .expectNextCount(1)
                .verifyComplete();
    }
    @Test
    void retrieveErrorCertTest() throws JsonProcessingException {
        when(properties.getResources()).thenReturn(new ConsumerProperties.Resources("localhost"));
        String data = "{ \"data\": { \"alertIndicators\": [ { \"alertType\": \"ALE\", \"customerMobileNumber\": \"3772056958\", \"customerEmail\": \"CARLOSPOSADA@BANCOLOMBIA.COM.CO\", \"pushActive\": \"0\"} ] } }";
        Response response= mapper.readValue(data, Response.class);
        response.getData().getAlertIndicators().get(0).setLastDataModificationDate(LocalDate.now());
        when(restConsumerIs.post(anyString(), any(), any(), any())).thenReturn(Mono.error(new Throwable("cert")));
        StepVerifier.create(clientAdapter.retrieveAlertInformation(client))
                .verifyErrorMatches(e -> e.getMessage().equals("cert"));
    }
    @Test
    void retrieveErrorNotFountCertTest() throws JsonProcessingException {
        when(properties.getResources()).thenReturn(new ConsumerProperties.Resources("localhost"));
        String data = "{ \"data\": { \"alertIndicators\": [ { \"alertType\": \"ALE\", \"customerMobileNumber\": \"3772056958\", \"customerEmail\": \"CARLOSPOSADA@BANCOLOMBIA.COM.CO\", \"pushActive\": \"0\"} ] } }";
        Response response= mapper.readValue(data, Response.class);
        response.getData().getAlertIndicators().get(0).setLastDataModificationDate(LocalDate.now());
        when(restConsumerIs.post(anyString(), any(), any(), any())).thenReturn(Mono.error(new Error(404,"not fount")));
        StepVerifier.create(clientAdapter.retrieveAlertInformation(client))
                .verifyComplete();
    }
    @Test
    void retrieveErrorUnauthorizedTest() throws JsonProcessingException {
        when(properties.getResources()).thenReturn(new ConsumerProperties.Resources("localhost"));
        String data = "{ \"data\": { \"alertIndicators\": [ { \"alertType\": \"ALE\", \"customerMobileNumber\": \"3772056958\", \"customerEmail\": \"CARLOSPOSADA@BANCOLOMBIA.COM.CO\", \"pushActive\": \"0\"} ] } }";
        Response response= mapper.readValue(data, Response.class);
        response.getData().getAlertIndicators().get(0).setLastDataModificationDate(LocalDate.now());
        when(restConsumerIs.post(anyString(), any(), any(), any())).thenReturn(Mono.error(new Error(401,"Not authorized")));
        StepVerifier.create(clientAdapter.retrieveAlertInformation(client))
                .verifyErrorMatches(e -> e.getMessage().equals(UNAUTHORIZED.getMessage()));
    }
    @Test
    void retrieveErrorAnyTest() throws JsonProcessingException {
        when(properties.getResources()).thenReturn(new ConsumerProperties.Resources("localhost"));
        String data = "{ \"data\": { \"alertIndicators\": [ { \"alertType\": \"ALE\", \"customerMobileNumber\": \"3772056958\", \"customerEmail\": \"CARLOSPOSADA@BANCOLOMBIA.COM.CO\", \"pushActive\": \"0\"} ] } }";
        Response response= mapper.readValue(data, Response.class);
        response.getData().getAlertIndicators().get(0).setLastDataModificationDate(LocalDate.now());
        when(restConsumerIs.post(anyString(), any(), any(), any())).thenReturn(Mono.error(new Error(500,new ResponseError(List.of(ResponseError.Error.builder().detail("Connection Error").build())))));
        StepVerifier.create(clientAdapter.retrieveAlertInformation(client))
                .verifyErrorMatches(e -> e.getMessage().equals("Connection Error"));
    }

}

