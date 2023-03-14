package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.consumer.RestClient;
import co.com.bancolombia.consumer.RestClientForm;
import co.com.bancolombia.consumer.adapter.response.Error;
import co.com.bancolombia.consumer.adapter.response.ErrorInfobipSMS;
import co.com.bancolombia.consumer.adapter.response.ErrorTokenInfobipRequest;
import co.com.bancolombia.consumer.adapter.response.SuccessInfobipSMS;
import co.com.bancolombia.consumer.adapter.response.model.TokenInfobipData;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.SMSInfobip;
import co.com.bancolombia.model.message.gateways.InfobipGateway;
import co.com.bancolombia.model.token.Account;
import co.com.bancolombia.model.token.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.TECHNICAL_EXCEPTION;

@Log
@Repository
@RequiredArgsConstructor
@Primary
public class InfobipAdapter implements InfobipGateway {

    private static final Integer STATUS_OK = 200;
    private final RestClientForm<TokenInfobipData,TokenInfobipData> clientToken;
    private final RestClient<SMSInfobip, SuccessInfobipSMS> clientSms;
    private final ConsumerProperties properties;


    @Override
    public Mono<Response> sendSMS(SMSInfobip smsInfobip) {
        return clientSms.post(properties.getResources().getEndpointInfobipSMS(), smsInfobip,
                        SuccessInfobipSMS.class,ErrorInfobipSMS.class)
                .map(response -> Response.builder().code(STATUS_OK)
                        .messages(response.getMessages()).build())
                .onErrorMap(Error.class,e -> new TechnicalException(((ErrorInfobipSMS) e.getData()).getRequestError().getServiceException().getText(),
                        TECHNICAL_EXCEPTION,e.getHttpsStatus()));
    }

    @Override
    public Mono<Token> getToken(Account account) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", account.getUsername());
        formData.add("client_secret", account.getPassword());
        formData.add("grant_type", "client_credentials");
        return Mono.just(new TokenInfobipData())
                .flatMap(requestTokenInfo->clientToken.post(properties.getResources().getEndpointInfobipToken(),
                        formData,TokenInfobipData.class, ErrorTokenInfobipRequest.class))
                .onErrorMap(Error.class,e -> new TechnicalException(((ErrorTokenInfobipRequest) e.getData()).getRequestError().getServiceException().getText(),
                        TECHNICAL_EXCEPTION,e.getHttpsStatus()))
                .flatMap(TokenInfobipData::toModel);
    }
}
