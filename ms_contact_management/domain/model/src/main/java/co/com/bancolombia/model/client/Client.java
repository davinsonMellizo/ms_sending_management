package co.com.bancolombia.model.client;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Client {
    private ClientId id;
    private String keyMdm;
}
