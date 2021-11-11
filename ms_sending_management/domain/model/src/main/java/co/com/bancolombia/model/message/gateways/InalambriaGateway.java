package co.com.bancolombia.model.message.gateways;

import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.SMSInalambria;
import reactor.core.publisher.Mono;

public interface InalambriaGateway {

    Mono<Response> sendSMS(SMSInalambria sms);
}
