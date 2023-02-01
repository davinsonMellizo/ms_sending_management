package co.com.bancolombia.model.message.gateways;

import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.SMSInfobip;
import co.com.bancolombia.model.token.Account;
import co.com.bancolombia.model.token.Token;
import reactor.core.publisher.Mono;

public interface InfobipGateway {
    Mono<Response> sendSMS(SMSInfobip smsInfobip);
    Mono<Token> getToken(Account account);
}
