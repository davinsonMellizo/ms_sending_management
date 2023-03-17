package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.consumer.RestClient;
import co.com.bancolombia.consumer.adapter.response.Error;
import co.com.bancolombia.consumer.adapter.response.ErrorMasivianSMS;
import co.com.bancolombia.consumer.adapter.response.ErrorTokenMasivRequest;
import co.com.bancolombia.consumer.adapter.response.SuccessMasivianSMS;
import co.com.bancolombia.consumer.adapter.response.model.TokenMasivData;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.SMSMasiv;
import co.com.bancolombia.model.message.gateways.MasivianGateway;
import co.com.bancolombia.model.token.Account;
import co.com.bancolombia.model.token.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.TECHNICAL_EXCEPTION;

@Repository
@RequiredArgsConstructor
public class MasivAdapter implements MasivianGateway {

    private static final Integer STATUS_OK = 200;
    private static final Integer STATUS_UNAUTHORIZED = 401;
    private final ConsumerProperties properties;
    private final RestClient<SMSMasiv, SuccessMasivianSMS> clientSms;
    private final RestClient<TokenMasivData,TokenMasivData> clientToken;
    private static final Integer CONSTANT = 3;

    @Override
    public Mono<Response> sendSMS(SMSMasiv smsMasiv) {
        return clientSms.post(properties.getResources().getEndpointMasivSms(), smsMasiv,
                SuccessMasivianSMS.class, ErrorMasivianSMS.class)
                .map(response -> Response.builder().code(STATUS_OK)
                        .description(response.getStatusMessage()).build())
                .onErrorResume(e -> (e instanceof Error) && ((Error) e).getHttpsStatus().equals(STATUS_UNAUTHORIZED),
                        e -> Mono.just(Response.builder()
                                .token(smsMasiv.getHeaders().toString())
                                .code( ((Error) e).getHttpsStatus()).description(((ErrorMasivianSMS)
                                        ((Error) e).getData()).getStatusMessage())
                                .build()))
                .onErrorMap(Error.class,e -> new TechnicalException(((ErrorMasivianSMS) e.getData()).getStatusMessage(),
                        TECHNICAL_EXCEPTION,e.getHttpsStatus()))
                .onErrorMap(e -> new TechnicalException(e.getMessage(),TECHNICAL_EXCEPTION,1));
    }

    @Override
    public Mono<Token> getToken(Account account){
        String headerValue = account.getUsername().concat(":").concat(account.getPassword());
        var headerValueEncode= Base64.getEncoder().encodeToString(headerValue.getBytes());
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Basic "+headerValueEncode);
        return Mono.just(new TokenMasivData())
                .map(requestTokenMasiv-> settingHeaders(headers, requestTokenMasiv))
                .flatMap(requestTokenMasiv->clientToken.post(properties.getResources().getEndpointMasivToken(),
                        requestTokenMasiv,TokenMasivData.class,ErrorTokenMasivRequest.class))
                .flatMap(TokenMasivData::toModel);

    }

    private TokenMasivData settingHeaders(Map<String, String> headers, TokenMasivData requestTokenMasiv) {
        requestTokenMasiv.setHeaders(headers);
        return requestTokenMasiv;
    }

}
