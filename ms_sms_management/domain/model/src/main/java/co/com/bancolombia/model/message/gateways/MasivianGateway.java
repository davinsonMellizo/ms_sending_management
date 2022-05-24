package co.com.bancolombia.model.message.gateways;

import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.SMSMasiv;
import co.com.bancolombia.model.token.Account;
import co.com.bancolombia.model.token.Token;
import reactor.core.publisher.Mono;

public interface MasivianGateway {

    Mono<Response> sendSMS(SMSMasiv smsMasiv);
    Mono<Token> getToken(Account account);
    //Mono<Token> postMasivProviderToken();//esta interfaz debe recibir el token
}
