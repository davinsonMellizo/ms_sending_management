package co.com.bancolombia.consumer.adapter.response.model;

import co.com.bancolombia.Request;
import co.com.bancolombia.model.token.RequestTokenInalambria;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RequestTokenInalambriaData extends Request {
    @JsonProperty("grant_type")
    private String grantType;
    @JsonProperty("refresh_token")
    private String refreshToken;

    public RequestTokenInalambriaData toData(RequestTokenInalambria requestTokenInalambria){
        this.setGrantType(requestTokenInalambria.getGrantType());
        this.setRefreshToken(requestTokenInalambria.getRefreshToken());
        return this;
    }
}
