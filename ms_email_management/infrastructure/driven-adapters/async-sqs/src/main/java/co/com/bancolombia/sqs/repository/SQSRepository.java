package co.com.bancolombia.sqs.repository;

import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import co.com.bancolombia.sqs.config.SQSProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Repository
@RequiredArgsConstructor
public class SQSRepository {
    private final SQSProperties properties;

    public final SqsAsyncClient client;

    private static final Logger LOGGER = LoggerFactory.getLogger(SQSRepository.class.getName());


    public Mono<Void> putQueue(String message) {
        return Mono.just(client.sendMessage(SendMessageRequest.builder()
                        .queueUrl(properties.getUrl())
                        .messageBody(message)
                        .build()))
                .onErrorResume(error -> {
                    LOGGER.info("message: " + TechnicalExceptionEnum.SEND_LOG_SQS_ERROR + " Detail: " + error);
                    return Mono.empty();
                })
                .then();
    }
}
