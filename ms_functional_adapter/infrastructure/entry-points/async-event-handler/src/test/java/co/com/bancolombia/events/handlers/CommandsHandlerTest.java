package co.com.bancolombia.events.handlers;

import co.com.bancolombia.events.model.ResourceQuery;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.usecase.functionaladapter.FunctionalAdapterUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.api.HandlerRegistry;
import org.reactivecommons.async.api.handlers.CommandHandler;
import reactor.core.publisher.Mono;

import reactor.test.StepVerifier;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CommandsHandlerTest {
    @InjectMocks
    private Handler handler;
    @Mock
    private FunctionalAdapterUseCase useCase;
    @Mock
    private LoggerBuilder logger;

    private HandlerRegistry register;
    private ResourceQuery.Resource resource;
    private ObjectMapper mapper;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        mapper = new ObjectMapper();
        register = HandlerRegistry.register();
    }

    @Test
    public void handleSendAlert() throws JsonProcessingException {
        when(useCase.sendTransactionToMQ(any())).thenReturn(Mono.empty());
        String config = "{\"data\":[{\"queryName\":\"transactions.mq.9369\",\"channel\":\"ALM\"," +
                "\"transaction\":\"9369\",\"template\":\"<#assign body = JsonUtil.jsonToMap(input)>${body.nro}  Mi nombre es ${body.name}\"},{\"queryName\":\"transactions.mq.9610\",\"channel\":\"ALM\",\"transaction\":\"9610\",\"template\":\"<#assign body = JsonUtil.jsonToMap(input)>${body.nro}  Mi nombre es ${body.name}\"}]}";
        ResourceQuery resourceQuery = mapper.readValue(config, ResourceQuery.class);
        resource  = resourceQuery.getData().get(0);
        handler.listenerMessage(resource, register);
        CommandHandler eventHandler = register.getCommandHandlers().get(0).getHandler();
        StepVerifier.create(eventHandler.handle(new Command<>("name", "001", Map.of("id", "1"))))
                .verifyComplete();
    }
}
