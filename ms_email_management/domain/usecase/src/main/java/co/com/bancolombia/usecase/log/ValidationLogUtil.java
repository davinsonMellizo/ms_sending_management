package co.com.bancolombia.usecase.log;


import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.TemplateEmail;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;


@UtilityClass
public class ValidationLogUtil {
    private static final int CODE_RESPONSE_200 = 200;
    private static final int CODE_RESPONSE_202 = 202;
    private static final int CODE_RESPONSE_0 = 0;

    public static final Predicate<Alert> isValidCount = alert ->
            (alert.getHeaders().containsKey("x-death")) && ((Object)alert.getHeaders().get("x-death")).toString()
                    .contains("count="+alert.getHeaders().get("retryNumber"));
    public static final Predicate<Response> validateCodeResponse = response ->
            ((response.getCode()!= CODE_RESPONSE_200) && (response.getCode()!= CODE_RESPONSE_202) && (response.getCode()!= CODE_RESPONSE_0));

public static  <T> Mono<T> validSendLog (Alert pAlert, String medium,  Response response,LogUseCase logUseCase, TemplateEmail template){

    return Mono.just(pAlert)
            .filter(alert-> response.getCode()== CODE_RESPONSE_200 || isValidCount.test(alert) || response.getCode()== CODE_RESPONSE_0)
            .flatMap(res->logUseCase.sendLog(pAlert,template,medium,response))
            .cast(Response.class)
            .switchIfEmpty(Mono.just(response))
            .filter(validateCodeResponse)
            .flatMap(res->Mono.error(new RuntimeException(res.getDescription())));
}



}
