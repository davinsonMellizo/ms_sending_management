package co.com.bancolombia.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class DataDTO {

    @Builder.Default
    private String consumer = "";
    @Builder.Default
    private String alert = "";
    @Builder.Default
    private String transactionCode = "";
    @Builder.Default
    private Long amount = 0L;

}
