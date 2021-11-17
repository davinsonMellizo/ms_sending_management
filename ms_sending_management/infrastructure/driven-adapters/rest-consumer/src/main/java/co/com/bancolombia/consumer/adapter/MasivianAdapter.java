package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.consumer.RestClient;
import co.com.bancolombia.consumer.adapter.response.*;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.message.Mail;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.SMS;
import co.com.bancolombia.model.message.gateways.MasivianGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class MasivianAdapter implements MasivianGateway {

    private static Integer STATUS_OK = 200;
    private static Integer STATUS_ERROR = 1;
    private final ConsumerProperties properties;
    private final RestClient<SMS, SuccessMasivianSMS> clientSms;
    private final RestClient<Mail, SuccessMasivianMAIL> clientMail;

    @Override
    public Mono<Response> sendSMS(SMS sms) {
        System.out.println(sms);
        return clientSms.post(properties.getResources().getEndpointMasivianSms(), sms,
                SuccessMasivianSMS.class, ErrorMasivianSMS.class)
                .map(response -> Response.builder().code(STATUS_OK)
                        .description(response.getDeliveryToken()).build())
                .onErrorResume(ErrorMasivianSMS.class, e -> Mono.just(Response.builder()
                        .code(e.getHttpsStatus()).description(e.getDescription())
                        .build()))
                .onErrorResume(WebClientException.class, e -> Mono.just(Response.builder()
                        .code(STATUS_ERROR).description(e.getMessage())
                        .build()));
    }

    @Override
    public Mono<Response> sendMAIL(Mail mail) {
        System.out.println(mail);
        return clientMail.post(properties.getResources().getEndpointMasivianMail(), mail,
                SuccessMasivianMAIL.class, ErrorMasivianMAIL.class)
                .map(response -> Response.builder().code(STATUS_OK)
                        .description(response.getDescription()).build())
                .onErrorResume(ErrorMasivianMAIL.class, e -> Mono.just(Response.builder()
                        .code(e.getHttpsStatus()).description(e.getDescription())
                        .build()))
                .onErrorResume(WebClientException.class, e -> Mono.just(Response.builder()
                        .code(STATUS_ERROR).description(e.getMessage())
                        .build()));
    }

}
