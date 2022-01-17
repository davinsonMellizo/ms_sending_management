package co.com.bancolombia.api.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.server.ServerRequest;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true)
public class ResponseDTO<T> {

    @Autowired
    private MetaDTO.Meta meta;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T object;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T error;

    public static <T> ResponseDTO success(String description, T object, ServerRequest request){
        return ResponseDTO.builder()
                .meta(MetaDTO.build(object, request))
                .data(ResponseDTO.builder().description(description).object(object).build())
                .build();
    }

    public static <T> ResponseDTO getSuccess(T data, ServerRequest request){
        return ResponseDTO.builder()
                .meta(MetaDTO.build(data, request))
                .data(data)
                .build();
    }

    public static <T> ResponseDTO failed(T error, ServerRequest request){
        return ResponseDTO.builder()
                .meta(MetaDTO.build(error, request))
                .error(error)
                .build();
    }
}
