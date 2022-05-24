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
public class TokenMasivData extends Request {
    @JsonProperty("idToken")
    private String accesToken;
    @JsonProperty("expirationTimeInSecs")
    private Long expiresIn;
    @JsonProperty("refreshToken")
    private String refreshToken;

    public Mono<Token> toModel(){
        return Mono.just(Token.builder().accessToken(this.accesToken).expiresIn(this.expiresIn)
                .refreshToken(this.refreshToken).build());
    }
}
