package co.com.bancolombia.api.commons.util;

import co.com.bancolombia.api.dto.AlertTransactionDTO;
import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@UtilityClass
public class ParamsUtil {

    public static final String ID = "id";
    public static final String ID_ALERT = "id-alert";
    public static final String ID_TRANSACTION = "id-transaction";
    public static final String ID_CONSUMER = "id-consumer";

    private static Mono<String> ofEmpty(String value) {
        return (value == null || value.isEmpty()) ? Mono.empty() : Mono.just(value);
    }

    public static Mono<String> getId(ServerRequest request) {
        return ofEmpty(request.pathVariable(ID));
    }

    public static Mono<AlertTransactionDTO> getRelationAlert(ServerRequest request) {
        return Mono.just(AlertTransactionDTO.builder()
                .idAlert(request.headers().firstHeader(ID_ALERT))
                .idTransaction(request.headers().firstHeader(ID_TRANSACTION))
                .idConsumer(request.headers().firstHeader(ID_CONSUMER))
                .build());
    }


}