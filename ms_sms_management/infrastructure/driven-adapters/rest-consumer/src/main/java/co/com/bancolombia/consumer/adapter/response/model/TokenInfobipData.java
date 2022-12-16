package co.com.bancolombia.consumer.adapter.response.model;

import co.com.bancolombia.consumer.adapter.mapper.Request;
import co.com.bancolombia.model.token.Token;
import co.com.bancolombia.model.token.TokenInfobip;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import reactor.core.publisher.Mono;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TokenInfobipData{
    @JsonProperty("access_token")
    public String accessToken;
    @JsonProperty("expires_in")
    public Long expiresIn;

    public Mono<Token> toModel(){
        return Mono.just(Token.builder().accessToken(this.accessToken).expiresIn(this.expiresIn).build());
    }
}
