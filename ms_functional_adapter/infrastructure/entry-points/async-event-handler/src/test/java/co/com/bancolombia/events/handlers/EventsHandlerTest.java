package co.com.bancolombia.events.handlers;

import co.com.bancolombia.events.HandlerRegistryConfiguration;
import co.com.bancolombia.events.model.ResourceQuery;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.model.transaction.Transaction;
import co.com.bancolombia.s3bucket.S3AsynOperations;
import co.com.bancolombia.usecase.functionaladapter.FunctionalAdapterUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.async.api.HandlerRegistry;
import org.reactivecommons.async.api.handlers.CommandHandler;
import org.reactivecommons.async.api.handlers.EventHandler;
import org.reactivecommons.async.impl.config.annotations.EnableEventListeners;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class EventsHandlerTest {

    @InjectMocks
    private HandlerRegistryConfiguration handlerRegistryConfiguration;
    @Mock
    private Handler handler;
    @Mock
    private S3AsynOperations s3AsynOperations;
    private String responseS3 = "{ \"data\": [ { \"queryName\": \"business.aliasidentity.register.*.distributionMicroservice.resgisterDone\", \"channel\": \"ALERTAS\", \"transaction\": \"0001\", \"template\": \"STIDQSG3EN-SG3\" }, { \"queryName\": \"business.aliasidentity.register.*.distributionMicroservice.resgisterRejected\", \"channel\": \"ALERTAS\", \"transaction\": \"0002\", \"template\": \"STIDQSG3EN-SG3\" } ] }";

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void handleSendAlert() {
        when(s3AsynOperations.getFileAsString(anyString(), anyString())).thenReturn(Mono.just(responseS3));
        StepVerifier.create(Mono.just(handlerRegistryConfiguration.queryHandler("sd", "sd")))
                .expectNextCount(1)
                .verifyComplete();
    }
}
