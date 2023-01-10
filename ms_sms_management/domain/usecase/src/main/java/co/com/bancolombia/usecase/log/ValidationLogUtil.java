package co.com.bancolombia.usecase.log;


import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Response;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;


@UtilityClass
public class ValidationLogUtil {
    private static final int CODE_RESPONSE_200 = 200;
    private static final int CODE_RESPONSE_202 = 202;

    public static final Predicate<Alert> isValidCount = alert ->
            (alert.getHeaders().containsKey("x-death")) && ((Object)alert.getHeaders().get("x-death")).toString()
                    .contains("count="+alert.getHeaders().get("retryNumber"));
    public static final Predicate<Response> validateCodeResponse = response ->
            ((response.getCode()!= CODE_RESPONSE_200) && (response.getCode()!= CODE_RESPONSE_202));

public static  <T> Mono<T> valideitorSendLog (Alert pAlert, String medium,  Response response,LogUseCase logUseCase){
    return Mono.just(pAlert)
            .filter(alert-> response.getCode()== CODE_RESPONSE_200 || isValidCount.test(alert))
            .flatMap(res->logUseCase.sendLog(pAlert,medium,response))
            .cast(Response.class)
            .switchIfEmpty(Mono.just(response))
            .filter(validateCodeResponse)
            .flatMap(res->Mono.error(new RuntimeException(res.getDescription())));
}



}
