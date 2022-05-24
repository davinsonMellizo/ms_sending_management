package co.com.bancolombia.model.token;

import co.com.bancolombia.Request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RequestTokenInalambria extends Request {
    private String grantType;
    private String refreshToken;
}
