package co.com.bancolombia.model.datatest;

import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.transaction.Transaction;

import java.util.Map;

public class FunctionalAdapterDataTest {

    public static Message buildMessage(){
        return Message.builder().from(buildFrom()).build();
    }

    public static Message.From buildFrom(){
        return Message.From.builder()
                .correlationID("a1sd23as1d2")
                .replyID("123456")
                .build();
    }

    public static Transaction buildTransaction(){
        return  Transaction.builder()
                .correlationID("a1sd23as1d2")
                .channel("ALM")
                .nroTransaction("123456")
                .from(buildFrom())
                .template("{ \"name\" : \"prueba\"}")
                .payload(Map.of("test","test"))
                .build();
    }
}
