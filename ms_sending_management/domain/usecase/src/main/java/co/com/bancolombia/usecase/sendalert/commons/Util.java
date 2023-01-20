package co.com.bancolombia.usecase.sendalert.commons;

import co.com.bancolombia.commons.enums.BusinessErrorMessage;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Parameter;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_PARAMETER;

@UtilityClass
public class Util {

    public Mono<Alert> replaceParameter(Alert alert, Message message) {
        return Flux.fromIterable(message.getParameters().entrySet().stream().collect(Collectors.toList()))
                .flatMap(parameter -> validateParameter(alert.getMessage(), parameter))
                .map(parameter -> alert.getMessage().replace("${"+parameter.getKey()+"}", parameter.getValue()))
                .doOnNext(alert::setMessage)
                .then(Mono.just(alert))
                .switchIfEmpty(Mono.just(alert))
                .filter(parameter -> !alert.getMessage().contains("${"))
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_PARAMETER)));
    }

    private Mono<Map.Entry<String, String>> validateParameter(String message, Map.Entry<String, String> pParameter){
        return Mono.just(pParameter)
                .filter(parameter -> message.contains(parameter.getKey()))
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_PARAMETER)));
    }

}
