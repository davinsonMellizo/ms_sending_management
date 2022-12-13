package co.com.bancolombia.consumer.adapter.response.model;

import co.com.bancolombia.Request;
import co.com.bancolombia.model.token.Token;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TokenInfobipData extends RequestForm {
    @JsonProperty("access_token")
    private String accesToken;
    @JsonProperty("expires_in")
    private Long expiresIn;

    public Mono<Token> toModel(){
        return Mono.just(Token.builder().accessToken(this.accesToken).expiresIn(this.expiresIn).build());
    }
}
