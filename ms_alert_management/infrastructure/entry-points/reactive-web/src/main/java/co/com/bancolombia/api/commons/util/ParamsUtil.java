package co.com.bancolombia.api.commons.util;

import co.com.bancolombia.api.dto.AlertClientDTO;
import co.com.bancolombia.api.dto.AlertTransactionDTO;
import co.com.bancolombia.api.dto.CampaignDTO;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static co.com.bancolombia.commons.constants.Header.*;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.*;

@UtilityClass
public class ParamsUtil {

    public static final String ID = "id";
    public static final String ID_ALERT = "id-alert";
    public static final String ID_TRANSACTION = "id-transaction";
    public static final String ID_CONSUMER = "id-consumer";
    public static final String NAME_TRIGGER = "nameTrigger";
    public static final String ID_CAMPAIGN = "id-campaign";

    private static Mono<String> ofEmptyHeaders(String value) {
        return (value == null || value.isEmpty()) ?
                Mono.error(new TechnicalException(HEADER_MISSING_ERROR)) : Mono.just(value);
    }

    private static Mono<String> ofEmptyParams(String value) {
        return (value == null || value.isEmpty()) ?
                Mono.error(new TechnicalException(PARAM_MISSING_ERROR)) : Mono.just(value);
    }

    public static Mono<String> getId(ServerRequest request) {
        return ofEmptyHeaders(request.pathVariable(ID));
    }

    public static Mono<AlertTransactionDTO> getRelationAlert(ServerRequest request) {
        return Mono.just(AlertTransactionDTO.builder()
                .idAlert(request.headers().firstHeader(ID_ALERT))
                .idTransaction(request.headers().firstHeader(ID_TRANSACTION))
                .idConsumer(request.headers().firstHeader(ID_CONSUMER))
                .build());
    }

    public static Mono<AlertClientDTO> getRelationClient(ServerRequest request) {
        return ofEmptyHeaders(request.headers().firstHeader(DOCUMENT_NUMBER))
                .zipWith(ofEmptyHeaders(request.headers().firstHeader(DOCUMENT_TYPE)))
                .filter(ParamsUtil::validateHeaders)
                .map(headers -> AlertClientDTO.builder()
                        .idAlert(request.headers().firstHeader(ID_ALERT))
                        .documentNumber(Long.valueOf(headers.getT1()))
                        .documentType(Integer.parseInt(headers.getT2()))
                        .build())
                .switchIfEmpty(Mono.error(new TechnicalException(INVALID_HEADER_ERROR)));

    }

    private static boolean validateHeaders(Tuple2<String, String> headers) {
        return Pattern.compile("^[0-9]+$").matcher(headers.getT1()).matches() &&
                Pattern.compile("^[0-9]+$").matcher(headers.getT2()).matches();
    }

    public static Mono<CampaignDTO> getCampaignParams(ServerRequest request) {
        return ofEmptyParams(request.queryParams().getFirst(ID_CAMPAIGN))
                .zipWith(ofEmptyParams(request.queryParams().getFirst(ID_CONSUMER)))
                .map(params -> CampaignDTO.builder()
                        .idCampaign(params.getT1())
                        .idConsumer(params.getT2())
                        .build());
    }

    public Map<String, String> setHeaders(ServerRequest serverRequest) {
        return serverRequest.headers()
                .asHttpHeaders()
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, v -> String.join(",", v.getValue())));
    }

    public Mono<Map<String, String>> validateHeaderBasicKit(ServerRequest request) {
        return Mono.just(setHeaders(request))
                .filter(headers -> headers.containsKey(DOCUMENT_TYPE))
                .filter(headers -> headers.containsKey(DOCUMENT_NUMBER))
                .filter(headers -> headers.containsKey(ASSOCIATION_ORIGIN));

    }

    public Mono<Map<String, String>> validateHeaderFindAlertClient(ServerRequest request) {
        return Mono.just(setHeaders(request))
                .filter(headers -> headers.containsKey(DOCUMENT_TYPE))
                .filter(headers -> headers.containsKey(DOCUMENT_NUMBER));

    }

    public static Mono<String> getTriggerParams(ServerRequest request) {
        return ofEmptyParams(request.queryParams().getFirst(NAME_TRIGGER));
    }
}