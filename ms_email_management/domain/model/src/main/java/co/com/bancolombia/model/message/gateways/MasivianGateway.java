package co.com.bancolombia.model.message.gateways;

import co.com.bancolombia.model.message.Mail;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.token.Account;
import co.com.bancolombia.model.token.Token;
import reactor.core.publisher.Mono;

public interface MasivianGateway {
    Mono<Response> sendMAIL(Mail mail);
    Mono<Token> getToken(Account account);
    Mono<Token> refreshToken(String requestTokenMasiv);
    Mono<String> generatePresignedUrl(String objectKey);
}
