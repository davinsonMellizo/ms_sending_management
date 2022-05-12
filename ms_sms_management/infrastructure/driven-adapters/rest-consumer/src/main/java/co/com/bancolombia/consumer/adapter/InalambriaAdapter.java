package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.consumer.RestClient;
import co.com.bancolombia.consumer.adapter.response.Error;
import co.com.bancolombia.consumer.adapter.response.ErrorInalambriaSMS;
import co.com.bancolombia.consumer.adapter.response.SuccessInalambriaSMS;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.SMSInalambria;
import co.com.bancolombia.model.message.gateways.InalambriaGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class InalambriaAdapter implements InalambriaGateway {

    private final static Integer STATUS_OK = 200;
    private final static Integer STATUS_ERROR = 1;
    private final ConsumerProperties properties;
    private final RestClient<SMSInalambria , SuccessInalambriaSMS> client;

    @Override
    public Mono<Response> sendSMS(SMSInalambria sms) {
        System.out.println("listo"+sms);
        return /*client.post(properties.getResources().getEndpointInalambriaSms(), sms,
                SuccessInalambriaSMS.class, ErrorInalambriaSMS.class)*/
                Mono.just(SuccessInalambriaSMS.builder().messageText("Success").build())
                .map(response -> Response.builder().code(STATUS_OK)
                        .description(response.getMessageText()).build())
                .onErrorResume(Error.class, e -> Mono.just(Response.builder()
                        .code(e.getHttpsStatus()).description(((ErrorInalambriaSMS)e.getData()).getMessageText())
                        .build()))
                .onErrorResume(e -> Mono.just(Response.builder()
                .code(STATUS_ERROR).description(e.getMessage())
                .build()));
    }
}
