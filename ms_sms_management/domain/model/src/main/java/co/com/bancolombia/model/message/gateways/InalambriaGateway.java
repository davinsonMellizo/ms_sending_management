package co.com.bancolombia.model.message.gateways;

import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.SMSInalambria;
import co.com.bancolombia.model.token.Account;
import co.com.bancolombia.model.token.RequestTokenInalambria;
import co.com.bancolombia.model.token.Token;
import reactor.core.publisher.Mono;

public interface InalambriaGateway {

    Mono<Response> sendSMS(SMSInalambria sms);
    Mono<Token> getToken(Account account);
    Mono<Token> refreshToken(RequestTokenInalambria requestTokenInalambria);

}
