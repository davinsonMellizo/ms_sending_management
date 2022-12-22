package co.com.bancolombia.usecase.log;


import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Response;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;


@UtilityClass
public class ValidationLogUtil {



public static  Mono<Void> valideitorSendLog (Alert alert, String medium,  Response response,LogUseCase logUseCase){
    if (response.getCode()!= 200 ){
        if (alert.getHeaders().containsKey("Count")){
            if (alert.getHeaders().get("Count").equals(alert.getHeaders().get("retryNumber"))){
                return logUseCase.sendLog(alert,medium,response);
            }
        }
    }else {
        return logUseCase.sendLog(alert,medium,response);
    }
    return logUseCase.sendLog(alert,medium,response);
}



}
