package co.com.bancolombia.model.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Token {
    private String accessToken;
    private Long expiresIn;
    private String refreshToken;
    private String tokenType;
}
