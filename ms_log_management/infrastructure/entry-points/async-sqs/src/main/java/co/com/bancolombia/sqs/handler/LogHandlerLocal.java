package co.com.bancolombia.sqs.handler;
import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.usecase.log.LogUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jashmore.sqs.argument.payload.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@EnableSqs
@Component
@Profile({"local"})
public class LogHandlerLocal {
   private final LogUseCase useCase;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SqsListener(value = "${cloud.aws.sqs.url}", deletionPolicy = SqsMessageDeletionPolicy.ALWAYS)
    public void listenLogBySqsListener(@Payload final String jsonMessage) throws JsonProcessingException {
        Log log =  objectMapper.readValue(jsonMessage, Log.class);
        useCase.saveLog(log).subscribe();
    }

}