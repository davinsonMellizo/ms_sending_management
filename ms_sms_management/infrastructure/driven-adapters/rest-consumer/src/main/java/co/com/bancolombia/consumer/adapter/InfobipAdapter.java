package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.consumer.RestClient;
import co.com.bancolombia.consumer.RestClientForm;
import co.com.bancolombia.consumer.adapter.response.*;
import co.com.bancolombia.consumer.adapter.response.Error;
import co.com.bancolombia.consumer.adapter.response.model.TokenInfobipData;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.SMSInfobip;
import co.com.bancolombia.model.message.SMSInfobipSDK;
import co.com.bancolombia.model.message.gateways.InfobipGateway;
import co.com.bancolombia.model.token.Account;
import co.com.bancolombia.model.token.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class InfobipAdapter implements InfobipGateway {

    private static final Integer STATUS_OK = 200;
    private final RestClientForm<TokenInfobipData,TokenInfobipData> clientToken;
    private final RestClient<SMSInfobip, SuccessInfobipSMS> clientSms;
    private final ConsumerProperties properties;
    private static final Integer CONSTANT = 3;

    @Override
    public Mono<Response> sendSMS(SMSInfobip smsInfobip) {
        return clientSms.post(properties.getResources().getEndpointInfobipSMS(), smsInfobip,
                        SuccessInfobipSMS.class,null)
                .map(response -> Response.builder().code(STATUS_OK)
                        .description(response.getDeliveryToken()).build())
                .onErrorResume(Error.class, e -> Mono.just(Response.builder()
                        .code(e.getHttpsStatus()).description(((ErrorMasivianSMS)e.getData()).getDescription())
                        .build()))
                .onErrorResume(e -> Mono.just(Response.builder()
                        .code(Integer.parseInt(e.getMessage().substring(0,CONSTANT))).description(e.getMessage())
                        .build()));
    }

    @Override
    public Mono<Token> getToken(Account account) {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", account.getUsername());
        formData.add("client_secret", account.getPassword());
        formData.add("grant_type", "client_credentials");

        return Mono.just(new TokenInfobipData())
                .map(requestTokenInfo-> settingForm(formData, requestTokenInfo))
                .flatMap(requestTokenInfo->clientToken.post(properties.getResources().getEndpointInfobipToken(),
                        requestTokenInfo,TokenInfobipData.class, ErrorTokenInfobipRequest.class))
                .flatMap(TokenInfobipData::toModel);
    }

    private TokenInfobipData settingForm(MultiValueMap<String, String> formData, TokenInfobipData tokenInfobipData) {
        tokenInfobipData.setForms(formData);
        return tokenInfobipData;
    }

    @Override
    public Mono<Response> sendSMSSDK(SMSInfobipSDK smsInfobip) {
        return null;
    }
}
