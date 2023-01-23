package co.com.bancolombia.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SmsDTO {

    @Builder.Default
    private String phone = "";
    @Builder.Default
    private String phoneIndicator = "";
    @Builder.Default
    private String url = "";
    private Integer priority;
}
