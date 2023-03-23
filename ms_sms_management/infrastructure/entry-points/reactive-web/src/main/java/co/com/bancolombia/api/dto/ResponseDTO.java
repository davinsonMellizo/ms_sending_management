package co.com.bancolombia.api.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true)
public class ResponseDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String statusCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

}
