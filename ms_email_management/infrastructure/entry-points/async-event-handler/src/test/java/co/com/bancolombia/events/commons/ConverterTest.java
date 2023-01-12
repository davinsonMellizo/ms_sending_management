package co.com.bancolombia.events.commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import reactor.test.StepVerifier;
import org.reactivecommons.async.impl.communications.Message;
import co.com.bancolombia.model.message.Alert;


import java.util.Map;



public class ConverterTest {

    @InjectMocks
    private Converter converter;

    public static final Map<String, Object> header = Map.of("key", "object");

    private Message message;

    @BeforeEach
    public void init() {

    }
    @Test
    public void converterMessageTest(){

        //StepVerifier.create(converter.converterMessage(message,Alert.class))
          //      .verifyComplete();

    }

}
