package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.consumer.RestClient;
import co.com.bancolombia.consumer.adapter.response.Error;
import co.com.bancolombia.consumer.adapter.response.ErrorMasivianMAIL;
import co.com.bancolombia.consumer.adapter.response.ErrorTokenMasivRequest;
import co.com.bancolombia.consumer.adapter.response.SuccessMasivianMAIL;
import co.com.bancolombia.consumer.adapter.response.model.TokenMasivData;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.message.Mail;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.gateways.MasivianGateway;
import co.com.bancolombia.model.token.Account;
import co.com.bancolombia.model.token.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class MasivianAdapter implements MasivianGateway {

    private final static Integer STATUS_OK = 200;
    private final static Integer STATUS_ERROR = 1;
    private final static int CONSTANT = 3;
    private final ConsumerProperties properties;
    private final RestClient<Mail, SuccessMasivianMAIL> clientMail;
    private final RestClient<TokenMasivData,TokenMasivData> clientToken;

    @Override
    public Mono<Response> sendMAIL(Mail mail) {
        String endpoint = properties.getResources().getEndpointMasivianMail();
        return clientMail.post(endpoint, mail,
                SuccessMasivianMAIL.class, ErrorMasivianMAIL.class)
                .map(response -> Response.builder().code(STATUS_OK)
                        .description(response.getDescription()).build())
                .onErrorResume(Error.class, e -> Mono.just(Response.builder()
                        .code(e.getHttpsStatus()).description(((ErrorMasivianMAIL)e.getData()).getDescription())
                        .build()))
                .onErrorResume(e -> Mono.just(Response.builder()
                            .description(e.getMessage())
                            .code(Integer.parseInt(e.getMessage().substring(0, CONSTANT)))
                            .build()));
    }

    @Override
    public Mono<Token> getToken(Account account) {
        String headerValue = account.getUsername().concat(":").concat(account.getPassword());
        String headerValueEncode= Base64.getEncoder().encodeToString(headerValue.getBytes());
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Basic "+headerValueEncode);
        return Mono.just(new TokenMasivData())
                .map(requestTokenMasiv->{requestTokenMasiv.setHeaders(headers);
                    return requestTokenMasiv;})
                .flatMap(requestTokenMasiv->clientToken.post(properties.getResources().getEndpointMasivToken(),
                        requestTokenMasiv,TokenMasivData.class,ErrorTokenMasivRequest.class))
                .flatMap(TokenMasivData::toModel);
    }

    @Override
    public Mono<Token> refreshToken(String requestTokenMasiv) {
        return null;
    }

}
