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
public class TokenInalambriaData extends Request {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private Long expiresIn;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("token_type")
    private String tokenType;

    public Mono<Token> toModel(){
        return Mono.just(Token.builder().accessToken(this.accessToken)
                .expiresIn(this.expiresIn).refreshToken(this.refreshToken).tokenType(this.tokenType).build());
    }
}
