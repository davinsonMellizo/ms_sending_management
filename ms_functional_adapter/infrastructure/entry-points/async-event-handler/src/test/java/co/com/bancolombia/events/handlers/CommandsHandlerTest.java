package co.com.bancolombia.events.handlers;

import co.com.bancolombia.commons.utils.JsonUtils;
import co.com.bancolombia.events.HandlerRegistryConfiguration;
import co.com.bancolombia.events.model.ResourceQuery;
import co.com.bancolombia.s3bucket.S3AsynOperations;
import co.com.bancolombia.usecase.functionaladapter.FunctionalAdapterUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.api.domain.Command;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class CommandsHandlerTest {
    /*@InjectMocks
    private Handler handler;
    @InjectMocks
    private HandlerRegistryConfiguration handlerRegistryConfiguration;
    @Mock
    private FunctionalAdapterUseCase useCase;
    @Mock
    private S3AsynOperations s3AsynOperations;
    @InjectMocks
    private JsonUtils jsonUtils;

    private String responseS3 = "{ \"data\": [ { \"typeEvent\": \"LISTEN_EVENT\", \"queryName\": \"business.aliasidentity.register.*.distributionMicroservice.resgisterDone\", \"channel\": \"ALERTAS\", \"transaction\": \"0001\", \"template\": \"STIDQSG3EN-SG3\" }, { \"typeEvent\": \"LISTEN_EVENT\", \"queryName\": \"business.aliasidentity.register.*.distributionMicroservice.resgisterRejected\", \"channel\": \"ALERTAS\", \"transaction\": \"0002\", \"template\": \"STIDQSG3EN-SG3\" } ] }";

    private ResourceQuery resourceQuery = new ResourceQuery();
    ResourceQuery.Resource resource = new ResourceQuery.Resource();

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        ArrayList data = new ArrayList();
        resource.setChannel("sdsd");
        resource.setTransaction("55");
        data.add(resource);
        resourceQuery.setData(data);
    }

    @Test
    public void handleSendAlert() {
        when(s3AsynOperations.getFileAsString(anyString(), anyString())).thenReturn(Mono.just(responseS3));
        when(JsonUtils.stringToType(responseS3, any())).thenReturn(resourceQuery);
        when(useCase.sendTransactionToMQ(any())).thenReturn(Mono.empty());
        StepVerifier.create(handlerRegistryConfiguration.handlerCommand(new Command<String>("alert", "alert", "data"), handler))
                .verifyComplete();
    }*/
}
