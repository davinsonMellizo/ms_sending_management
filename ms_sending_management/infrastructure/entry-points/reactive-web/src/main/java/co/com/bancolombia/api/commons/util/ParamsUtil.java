package co.com.bancolombia.api.commons.util;

import co.com.bancolombia.api.dto.AlertTransactionDTO;
import co.com.bancolombia.api.headers.AlertClientHeader;
import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.Optional;

@UtilityClass
public class ParamsUtil {

    public static final String ID = "id";
    public static final String ID_ALERT = "id-alert";
    public static final String ID_TRANSACTION = "id-transaction";
    public static final String ID_CONSUMER = "id-consumer";
    public static final String DOCUMENT_NUMBER = "document-number";
    public static final String DOCUMENT_TYPE = "document-type";

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

    public static Mono<AlertClientHeader> getClientHeaders(ServerRequest request) {
        return Mono.just(AlertClientHeader.builder()
                .idAlert(getHeader(request, ID_ALERT))
                .documentNumber(getHeader(request, DOCUMENT_NUMBER))
                .documentType(getHeader(request, DOCUMENT_TYPE))
                .build());
    }

    public static Mono<AlertClientHeader> getClientHeadersFind(ServerRequest request) {
        return Mono.just(AlertClientHeader.builder()
                .documentNumber(getHeader(request, DOCUMENT_NUMBER))
                .documentType(getHeader(request, DOCUMENT_TYPE))
                .build());
    }

    public static String getHeader(ServerRequest request, String header) {
        return ofEmptyHeader(request.headers().firstHeader(header)).orElse("Undefined");
    }

    private static Optional<String> ofEmptyHeader(String value) {
        return (value == null || value.isEmpty()) ? Optional.empty() : Optional.of(value);
    }

}