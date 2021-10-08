package co.com.bancolombia.api.commons.util;

import co.com.bancolombia.api.header.ClientHeader;
import co.com.bancolombia.api.header.ContactHeader;
import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static co.com.bancolombia.commons.enums.Header.*;

@UtilityClass
public class ParamsUtil {

    private static Optional<String> ofEmpty(String value) {
        return (value == null || value.isEmpty()) ? Optional.empty() : Optional.of(value);
    }

    public static Mono<ClientHeader> getClientHeaders(ServerRequest request) {
        return Mono.just(ClientHeader.builder()
                .documentNumber(getHeader(request, DOCUMENT_NUMBER))
                .documentType(getHeader(request, DOCUMENT_TYPE))
                .build());
    }

    public static Mono<ContactHeader> getContactHeaders(ServerRequest request) {
        return Mono.just(ContactHeader.builder()
                .documentNumber(getHeader(request, DOCUMENT_NUMBER))
                .documentType(getHeader(request, DOCUMENT_TYPE))
                .contactMedium(getHeader(request, CONTACT_MEDIUM))
                .consumer(getHeader(request, SEGMENT))
                .build());
    }

    public static String getHeader(ServerRequest request, String header) {
        return ofEmpty(request.headers().firstHeader(header)).orElse("");
    }

    public String getConsumer(ServerRequest request){
        return getHeader(request, CONSUMER);
    }

}