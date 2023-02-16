package co.com.bancolombia.api.commons.handlers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Error implements Serializable {

    private String reason;
    private String domain;
    private String code;
    private String message;
    @JsonIgnore
    private String httpStatus;
}
