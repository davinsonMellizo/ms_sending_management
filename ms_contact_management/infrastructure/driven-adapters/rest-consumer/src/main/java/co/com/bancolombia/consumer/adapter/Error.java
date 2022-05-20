package co.com.bancolombia.consumer.adapter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Error extends Throwable{
    private Integer httpsStatus;
    private Object data;
}
