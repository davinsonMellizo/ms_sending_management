package co.com.bancolombia.api.utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ErrorDTO {
    private String code;
    private String type;
    private String title;
    private String detail;
    private String source;
}
