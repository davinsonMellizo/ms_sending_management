package co.com.bancolombia.sqs.listener;

import co.com.bancolombia.model.message.SqsFileMessage;
import co.com.bancolombia.usecase.sendalert.SendAlertUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class SQSProcessor implements Function<Message, Mono<Void>> {
    private final SendAlertUseCase useCase;
    private final ObjectMapper objectMapper;
    @Override
    public Mono<Void> apply(Message message) {
        return Mono.fromCallable(() -> objectMapper.readTree(message.body()))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(jsonNode -> Mono.just(jsonNode.get("Records").get(0).get("s3")))
                .map(jsonNode -> SqsFileMessage
                        .builder()
                        .messageId(message.messageId())
                        .bucketName(jsonNode.get("bucket").get("name").asText())
                        .fileName(jsonNode.get("object").get("key").asText().replace("%3D", "="))
                        .build())
                .flatMap(useCase::getTemplate).then();
    }

}
