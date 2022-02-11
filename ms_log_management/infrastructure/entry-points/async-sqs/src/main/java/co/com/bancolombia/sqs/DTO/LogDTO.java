package co.com.bancolombia.sqs.DTO;

import co.com.bancolombia.model.log.Log;

public class LogDTO {

    public Log toModel(){
        return Log.builder().build();
    }
}
