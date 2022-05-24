package co.com.bancolombia.model.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TokenCacheIna implements Serializable {
    private List<String> tokensIna;

    public TokenCacheIna setTokensIna(String tokenIna) {
        this.tokensIna.add(0,tokenIna);
        return this;
    }
}
