package co.com.bancolombia.events.handlers;

import co.com.bancolombia.events.HandlerRegistryConfiguration;
import co.com.bancolombia.s3bucket.S3AsyncOperations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class EventsHandlerTest {

    @InjectMocks
    private HandlerRegistryConfiguration handlerRegistryConfiguration;
    @Mock
    private Handler handler;
    @Mock
    private S3AsyncOperations s3AsyncOperations;
    private String responseS3 = "{ \"data\": [ { \"queryName\": \"business.aliasidentity.register.*.distributionMicroservice.resgisterDone\", \"channel\": \"ALERTAS\", \"transaction\": \"0001\", \"template\": \"STIDQSG3EN-SG3\" }, { \"queryName\": \"business.aliasidentity.register.*.distributionMicroservice.resgisterRejected\", \"channel\": \"ALERTAS\", \"transaction\": \"0002\", \"template\": \"STIDQSG3EN-SG3\" } ] }";

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    void handleSendAlert() {
        when(s3AsyncOperations.getFileAsString(anyString(), anyString())).thenReturn(Mono.just(responseS3));
        StepVerifier.create(Mono.just(handlerRegistryConfiguration.queryHandler("sd", "sd")))
                .expectNextCount(1)
                .verifyComplete();
    }
}
