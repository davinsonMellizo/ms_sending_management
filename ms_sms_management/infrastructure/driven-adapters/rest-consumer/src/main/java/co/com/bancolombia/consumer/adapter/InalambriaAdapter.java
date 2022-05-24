package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.consumer.RestClient;
import co.com.bancolombia.consumer.adapter.response.Error;
import co.com.bancolombia.consumer.adapter.response.ErrorInalambriaSMS;
import co.com.bancolombia.consumer.adapter.response.ErrorTokenRefreshInalambria;
import co.com.bancolombia.consumer.adapter.response.SuccessInalambriaSMS;
import co.com.bancolombia.consumer.adapter.response.model.RequestTokenInalambriaData;
import co.com.bancolombia.consumer.adapter.response.model.TokenInalambriaData;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.SMSInalambria;
import co.com.bancolombia.model.message.gateways.InalambriaGateway;
import co.com.bancolombia.model.token.Account;
import co.com.bancolombia.model.token.RequestTokenInalambria;
import co.com.bancolombia.model.token.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static co.com.bancolombia.commons.constants.GrandTypeToken.GRAND_TYPE;

@Repository
@RequiredArgsConstructor
public class InalambriaAdapter implements InalambriaGateway {

    private static final Integer STATUS_OK = 200;
    private static final Integer STATUS_ERROR = 1;
    private final ConsumerProperties properties;
    private final RestClient<SMSInalambria , SuccessInalambriaSMS> client;
    private final RestClient<RequestTokenInalambriaData, TokenInalambriaData> clientToken;
    private static final Integer CONSTANT = 3;
    private static final Integer CONSTANT2 = 1000;

    @Override
    public Mono<Response> sendSMS(SMSInalambria sms) {
        System.out.println("listo"+sms);
        return client.post(properties.getResources().getEndpointInalambriaSms(), sms,
                SuccessInalambriaSMS.class, ErrorInalambriaSMS.class)
                //Mono.just(SuccessInalambriaSMS.builder().messageText("Success").build())
                .map(response -> Response.builder().code(STATUS_OK)
                        .description(response.getMessageText()).build())
                .onErrorResume(Error.class, e -> Mono.just(Response.builder()
                            .token(sms.getHeaders().toString())
                            .code(e.getHttpsStatus()).description(((ErrorInalambriaSMS)e.getData()).getMessageText())
                            .build()))
                .onErrorResume(e ->Mono.just(Response.builder()
                            .code(Integer.parseInt(e.getMessage().substring(0, CONSTANT))).description(e.getMessage())
                            .build()));
    }

    @Override
    public Mono<Token> getToken(Account account) {
        String headerValue = account.getUsername().concat(":").concat(account.getPassword());
        var headerValueEncode= Base64.getEncoder().encodeToString(headerValue.getBytes());
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Basic "+headerValueEncode);
        return Mono.just(RequestTokenInalambriaData.builder().grantType(GRAND_TYPE).build())
                .map(requestTokenInalambria-> settingHeaders(headers, requestTokenInalambria))
                .flatMap(requestTokenInalambria1 -> clientToken.post(properties.getResources()
                                .getEndpointInalambriaToken(), requestTokenInalambria1,
                        TokenInalambriaData.class, ErrorTokenRefreshInalambria.class))
                .flatMap(TokenInalambriaData::toModel)
                .flatMap(this::setExpiresIn);
    }

    private RequestTokenInalambriaData settingHeaders(Map<String, String> headers,
                                                      RequestTokenInalambriaData requestTokenInalambria) {
        requestTokenInalambria.setHeaders(headers);
        return requestTokenInalambria;
    }

    private Mono<Token> setExpiresIn(Token token) {
        return Mono.just(token.getExpiresIn())
                .map(expiresIn->(expiresIn*CONSTANT2)+System.currentTimeMillis())
                .map(expiresIn ->token.toBuilder()
                        .expiresIn(expiresIn).build());
    }

    @Override
    public Mono<Token> refreshToken(RequestTokenInalambria refreshTokenInalambria) {
        return Mono.just(RequestTokenInalambriaData.builder().build().toData(refreshTokenInalambria))
                .flatMap(requestTokenInalambriaData ->clientToken.post(properties.getResources()
                                .getEndpointInalambriaRefreshToken(), requestTokenInalambriaData,
                        TokenInalambriaData.class, ErrorTokenRefreshInalambria.class) )
                .flatMap(TokenInalambriaData::toModel)
                .onErrorResume(error -> Mono.empty());
    }


}
