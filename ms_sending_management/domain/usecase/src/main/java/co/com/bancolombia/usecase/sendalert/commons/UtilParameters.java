package co.com.bancolombia.usecase.sendalert.commons;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.message.Message;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_PARAMETER;
import static co.com.bancolombia.usecase.sendalert.commons.ValidateData.containParameter;

@UtilityClass
public class UtilParameters {

    public Mono<Alert> validateMessageAlert(Alert alert, Message message) {
        return Mono.just(alert)
                .filter(alert1 -> !alert.getMessage().isEmpty())
                .flatMap(alert1 -> replaceParameter(alert, message))
                .switchIfEmpty(Mono.just(alert));
    }
    private Mono<Alert> replaceParameter(Alert alert, Message message) {
        return Mono.just(message.getParameters().values())
                .map(ArrayList::new)
                .map(parameters -> IntStream.range(0, parameters.size()).boxed()
                        .collect(Collectors.toMap(i -> i + 1, parameters::get)))
                .map(Map::entrySet)
                .map(ArrayList::new)
                .flatMapMany(Flux::fromIterable)
                .flatMap(parameter -> validateParameter(alert.getMessage(), parameter))
                .map(parameter -> alert.getMessage().replace("<C"+parameter.getKey()+">", parameter.getValue()))
                .doOnNext(alert::setMessage)
                .then(Mono.just(alert))
                .switchIfEmpty(Mono.just(alert))
                .filter(containParameter)
                .flatMap(alert1 -> setParameters(alert1, message))
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_PARAMETER)));
    }

    private Mono<Map.Entry<Integer, String>> validateParameter(String message, Map.Entry<Integer, String> pParameter){
        return Mono.just(pParameter)
                .filter(parameter -> message.contains("<C"+parameter.getKey()+">"))
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_PARAMETER)));
    }

    private Mono<Alert>  setParameters(Alert alert, Message message){
        return Mono.just(new HashMap<String, String>())
                .doOnNext(parameters -> parameters.put("message", alert.getMessage()))
                .doOnNext(message::setParameters)
                .thenReturn(alert);
        }

}
