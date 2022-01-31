package co.com.bancolombia.events.handlers;

import org.mockito.InjectMocks;

import static org.mockito.ArgumentMatchers.any;

public class CommandsHandlerTest {
    @InjectMocks
    private Handler handler;
   /* @Mock
    private SendAlertUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void handleSendAlert() {
        when(useCase.sendAlert(any())).thenReturn(Mono.empty());
        StepVerifier.create(commandsHandler.handleSendAlert(new Command<Alert>("alert", "alert",
                Alert.builder().build())))
                .verifyComplete();
    }*/
}
