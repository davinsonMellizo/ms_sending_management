package co.com.bancolombia.api.commons.util;

import co.com.bancolombia.api.dto.AlertClientDTO;
import co.com.bancolombia.api.dto.AlertTransactionDTO;
import co.com.bancolombia.api.dto.ProviderServiceDTO;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

import static co.com.bancolombia.commons.constants.Header.*;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.HEADER_MISSING_ERROR;

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
        return (value == null || value.isEmpty()) ?
                Mono.error(new TechnicalException(HEADER_MISSING_ERROR)) : Mono.just(value);
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

    public Map<String, String> setHeaders(ServerRequest serverRequest){
        return serverRequest.headers()
                .asHttpHeaders()
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, v -> String.join(",", v.getValue())));
    }

    public Mono<Map<String, String>> validateHeader(ServerRequest request){
        return Mono.just(setHeaders(request))
                .map(stringStringMap -> {
                    System.out.println("headeres "+stringStringMap);
                    return stringStringMap;
                })
                .filter(headers -> headers.containsKey(DOCUMENT_TYPE))
                .filter(headers -> headers.containsKey(DOCUMENT_NUMBER))
                .filter(headers -> headers.containsKey(ASSOCIATION_ORIGIN));

    }

}