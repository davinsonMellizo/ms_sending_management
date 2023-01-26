package co.com.bancolombia.consumer.adapter.mapper;

import lombok.Data;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;
import java.util.Map;

@Data
public class RequestForm implements Serializable {
    private MultiValueMap<String, String> forms;
    private Map<String, String> headers;
}
