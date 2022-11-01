package co.com.bancolombia.api.utils;

import co.com.bancolombia.api.dto.GetTemplateDTO;
import co.com.bancolombia.api.dto.MessageDTO;
import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@UtilityClass
public class Params {

    public static final String ID_TEMPLATE = "idTemplate";

    public static Mono<GetTemplateDTO> getTemplateParams(ServerRequest request) {
        return Mono.just(GetTemplateDTO.builder()
                .idTemplate(request.queryParams().getFirst(ID_TEMPLATE))
                .build());
    }

    public static Mono<MessageDTO> getMessageParams(ServerRequest request) {
        return request.bodyToMono(HashMap.class)
                .map(hashMap -> MessageDTO.builder()
                        .idTemplate(request.queryParams().getFirst(ID_TEMPLATE))
                        .messageValues(hashMap)
                        .build());
    }
}
