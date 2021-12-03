package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.model.message.Mail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MasivianAdapterTest {

    @InjectMocks
    private MasivianAdapter masivianAdapter;
    @Mock
    private WebClient webClient;

    @Test
    public void sendMAILTest(){

    }


}
