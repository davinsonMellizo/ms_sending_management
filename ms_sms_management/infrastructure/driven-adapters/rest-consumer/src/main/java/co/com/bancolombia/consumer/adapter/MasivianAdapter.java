package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.consumer.RestClient;
import co.com.bancolombia.consumer.adapter.response.Error;
import co.com.bancolombia.consumer.adapter.response.*;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.Sms;
import co.com.bancolombia.model.message.gateways.MasivianGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class MasivianAdapter implements MasivianGateway {

    private final static Integer STATUS_OK = 200;
    private final static Integer STATUS_ERROR = 1;
    private final ConsumerProperties properties;
    private final RestClient<Sms, SuccessMasivianSMS> clientSms;

    @Override
    public Mono<Response> sendSMS(Sms sms) {
        System.out.println(sms);
        return clientSms.post(properties.getResources().getEndpointMasivianSms(), sms,
                SuccessMasivianSMS.class, ErrorMasivianSMS.class)
                .doOnNext(System.out::println)
                .map(response -> Response.builder().code(STATUS_OK)
                        .description(response.getDeliveryToken()).build())
                .onErrorResume(Error.class, e -> Mono.just(Response.builder()
                        .code(e.getHttpsStatus()).description(((ErrorMasivianSMS)e.getData()).getDescription())
                        .build()))
                .onErrorResume(e -> Mono.just(Response.builder()
                        .code(STATUS_ERROR).description(e.getMessage())
                        .build()));
    }

}
