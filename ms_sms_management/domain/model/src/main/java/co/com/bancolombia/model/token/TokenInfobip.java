package co.com.bancolombia.model.token;

import co.com.bancolombia.Request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TokenInfobip extends Request {
    private String accessToken;
    private String expiresIn;
}
