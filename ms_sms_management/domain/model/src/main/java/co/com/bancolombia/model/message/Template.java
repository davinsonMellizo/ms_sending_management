package co.com.bancolombia.model.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Template {
<<<<<<< HEAD
    private Map<String,Object> parameters;
=======
    private Map<String, String> parameters;
>>>>>>> 93d5ab90ae225951ae542f2e8d7dfc37e6d2c286
    private String name;
}
