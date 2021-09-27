package co.com.bancolombia.sqs.handler;
import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.usecase.log.LogUseCase;
import com.jashmore.sqs.argument.payload.Payload;
import com.jashmore.sqs.spring.container.basic.QueueListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Repository
@Component
@Profile({"dev", "qa", "pdn"})
public class LogHandler{
    private final LogUseCase useCase;

    @QueueListener(value = "${cloud.aws.sqs.queue-endpoint}", concurrencyLevel = 1000)
    public void listenLogByQueueListener(@Payload final Log log) {
        System.out.println("Mensaje leido, procediendo a enviar"+log);
        useCase.saveLog(log).subscribe();
    }
}