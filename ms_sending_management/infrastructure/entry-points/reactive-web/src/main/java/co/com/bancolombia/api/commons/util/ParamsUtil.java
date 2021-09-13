package co.com.bancolombia.api.commons.util;

import co.com.bancolombia.api.dto.AlertClientDTO;
import co.com.bancolombia.api.dto.AlertTransactionDTO;
import co.com.bancolombia.api.dto.ProviderServiceDTO;
import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@UtilityClass
public class ParamsUtil {

    public static final String ID = "id";
    public static final String ID_ALERT = "id-alert";
    public static final String ID_TRANSACTION = "id-transaction";
    public static final String ID_CONSUMER = "id-consumer";
    public static final String ID_CLIENT = "id-client";
    public static final String ID_PROVIDER = "id-provider";
    public static final String ID_SERVICE = "id-service";

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

    public static Mono<AlertClientDTO> getRelationClient(ServerRequest request) {
        return Mono.just(AlertClientDTO.builder()
                .idAlert(request.headers().firstHeader(ID_ALERT))
                .idClient(Integer.parseInt(request.headers().firstHeader(ID_CLIENT)))
                .build());
    }

    public static Mono<ProviderServiceDTO> getRelationProvider(ServerRequest request) {
        return Mono.just(ProviderServiceDTO.builder()
                .idProvider(request.headers().firstHeader(ID_PROVIDER))
                .idService(Integer.parseInt(request.headers().firstHeader(ID_SERVICE)))
                .build());
    }

}