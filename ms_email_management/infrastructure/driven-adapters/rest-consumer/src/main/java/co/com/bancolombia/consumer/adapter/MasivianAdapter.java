package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.consumer.RestClient;
import co.com.bancolombia.consumer.adapter.response.Error;
import co.com.bancolombia.consumer.adapter.response.*;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.message.Mail;
import co.com.bancolombia.model.message.Response;
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
    private final RestClient<Mail, SuccessMasivianMAIL> clientMail;

    @Override
    public Mono<Response> sendMAIL(Mail mail) {
        System.out.println(mail);
        String endpoint = properties.getResources().getEndpointMasivianMail();
        return clientMail.post(endpoint, mail,
                SuccessMasivianMAIL.class, ErrorMasivianMAIL.class)
                .map(response -> Response.builder().code(STATUS_OK)
                        .description(response.getDescription()).build())
                .onErrorResume(Error.class, e -> Mono.just(Response.builder()
                        .code(e.getHttpsStatus()).description(((ErrorMasivianMAIL)e.getData()).getDescription())
                        .build()))
                .onErrorResume(e -> Mono.just(Response.builder()
                        .code(STATUS_ERROR).description(e.getMessage())
                        .build()));
    }

}
