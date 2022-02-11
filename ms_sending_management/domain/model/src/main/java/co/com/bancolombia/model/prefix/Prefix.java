package co.com.bancolombia.model.prefix;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Prefix {
    private String code;
}
