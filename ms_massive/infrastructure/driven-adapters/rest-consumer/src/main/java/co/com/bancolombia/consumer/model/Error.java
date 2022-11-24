package co.com.bancolombia.consumer.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Error extends Throwable {

    private final Integer status;
    private final Object data;

}
