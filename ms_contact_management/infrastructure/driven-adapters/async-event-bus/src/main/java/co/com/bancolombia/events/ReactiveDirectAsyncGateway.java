package co.com.bancolombia.events;

import co.com.bancolombia.model.bridge.Bridge;
import co.com.bancolombia.model.events.gateways.CommandGateway;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.api.DirectAsyncGateway;
import org.reactivecommons.async.impl.config.annotations.EnableDirectAsyncGateway;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@EnableDirectAsyncGateway
public class ReactiveDirectAsyncGateway implements CommandGateway {

    public static final String TARGET_NAME_TEST = "ms_functional_adapter";
    public static final String SEND_CREATE_ISERIES = "send.create.iseries";
    public static final String SEND_UPDATE_ISERIES = "send.update.iseries";

    private final DirectAsyncGateway gateway;

    @Override
    public Mono<Void> sendCommandEnroll(Bridge bridge) {
        return gateway.sendCommand(new Command<>(SEND_CREATE_ISERIES, UUID.randomUUID().toString(),
                        getValuesToSend(bridge)), TARGET_NAME_TEST)
                .then(Mono.empty());
    }

    @Override
    public Mono<Void> sendCommandUpdate(Bridge bridge) {
        return gateway.sendCommand(new Command<>(SEND_UPDATE_ISERIES, UUID.randomUUID().toString(),
                        getValuesToSend(bridge)), TARGET_NAME_TEST)
                .then(Mono.empty());
    }

    private Map<String, String> getValuesToSend(Bridge bridge) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.convertValue(bridge, Map.class);
        return map.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().isEmpty()
                        ? " " : entry.getValue()));
    }
}
