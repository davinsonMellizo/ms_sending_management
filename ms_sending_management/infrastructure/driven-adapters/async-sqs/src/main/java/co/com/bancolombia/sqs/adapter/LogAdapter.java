package co.com.bancolombia.sqs.adapter;

import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.log.gateways.LogGateway;
import co.com.bancolombia.sqs.commons.Util;
import co.com.bancolombia.sqs.repository.SQSRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LogAdapter implements LogGateway {
    private final SQSRepository sqsRepository;

    @Override
    public Mono<Log> putLogToSQS(Log log) {
        return Mono.just(log)
                .map(Util::convertToJson)
                //.flatMap(sqsRepository::putQueue)
                .flatMap(s -> Mono.empty())
                .thenReturn(log);
    }
}
