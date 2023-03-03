package co.com.bancolombia.rabbitmq.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.async.commons.communications.Message;
import org.reactivecommons.async.commons.exceptions.MessageConversionException;
import org.reactivecommons.async.rabbit.RabbitMessage;

import static org.assertj.core.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class JacksonMessageConverterTest {

    private ObjectMapper objectMapper =new ObjectMapper();

    private  JacksonMessageConverter jacksonMessageConverter = new JacksonMessageConverter(objectMapper);
    private Message.Properties properties= new RabbitMessage.RabbitMessageProperties();
    private String commandString = "{ \"name\": \"send\", \"commandId\": \"94\",\"data\": \"data\" }";
    private String commandStringError = "";
    private String commandStringDomainEvent = "{ \"name\": \"send\", \"eventId\": \"94\",\"data\": \"data\" }";

    private String commandStringReadAsyncQuery = "{ \"resource\": \"send\", \"queryData\": \"data\" }";


    private Message message = new RabbitMessage(commandString.getBytes(),properties);

    private Message messageError = new RabbitMessage(commandStringError.getBytes(),properties);
    private Message messageDomainEvent = new RabbitMessage(commandStringDomainEvent.getBytes(),properties);
    private Message messageReadAsyncQuery = new RabbitMessage(commandStringReadAsyncQuery.getBytes(),properties);

    @Test
    void readCommandTest (){
        assertThat(jacksonMessageConverter.readCommand(message,String.class)).isNotNull();
    }
    @Test
    void readCommandErrorTest (){
        assertThatThrownBy(() -> jacksonMessageConverter.readCommand(messageError, String.class))
                .isInstanceOf(MessageConversionException.class);
    }
    @Test
    void readDomainEventTest (){
        assertThat(jacksonMessageConverter.readDomainEvent(messageDomainEvent,String.class)).isNotNull();
    }
    @Test
    void readDomainEventErrorTest (){
        assertThatThrownBy(() -> jacksonMessageConverter.readDomainEvent(messageError, String.class))
                .isInstanceOf(MessageConversionException.class);
    }
    @Test
    void readAsyncQueryTest (){
        assertThat(jacksonMessageConverter.readAsyncQuery(messageReadAsyncQuery,String.class)).isNotNull();
    }
    @Test
    void readAsyncQueryErrorTest (){
        assertThatThrownBy(() -> jacksonMessageConverter.readAsyncQuery(messageError, String.class))
                .isInstanceOf(MessageConversionException.class);
    }


    @Test
    void readValueErrorTest (){
        assertThatThrownBy(() -> jacksonMessageConverter.readValue(messageError, String.class))
                .isInstanceOf(MessageConversionException.class);
    }
    @Test
    void readAsyncQueryStructureTest (){
        assertThat(jacksonMessageConverter.readAsyncQueryStructure(messageReadAsyncQuery)).isNotNull();
    }

    @Test
    void toMessageTest (){
        assertThat(jacksonMessageConverter.toMessage(message)).isNotNull();
    }
    @Test
    void toMessageErrorTest (){
        assertThatThrownBy(() -> jacksonMessageConverter.toMessage(anyOf()))
                .isInstanceOf(MessageConversionException.class);
    }

}


