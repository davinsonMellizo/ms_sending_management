package co.com.bancolombia.usecase;

import co.com.bancolombia.commons.enums.BusinessErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Response {
    private Integer code;
    private String description;

    public Response(Integer code, BusinessErrorMessage message){
        this.code = code;
        this.description = message.getMessage();
    }
}
