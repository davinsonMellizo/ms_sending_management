package co.com.bancolombia.sqs.repository;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.sqs.config.SQSProperties;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.SEND_LOG_SQS_ERROR;

@Repository
@RequiredArgsConstructor
public class SQSRepository {
    private final SQSProperties properties;
    private final AmazonSQSAsync client;

    public Mono<Void> putQueue(String message) {
        return Mono.just(new SendMessageRequest()
                .withQueueUrl(properties.getUrl())
                .withMessageBody(message))
                .map(client::sendMessage)
                .onErrorMap(error -> new TechnicalException(error, SEND_LOG_SQS_ERROR))
                .then();
    }
}
