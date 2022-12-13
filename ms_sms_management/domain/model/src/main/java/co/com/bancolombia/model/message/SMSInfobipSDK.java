package co.com.bancolombia.model.message;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true)
public class SMSInfobipSDK {
    private String from;
    private String to;
    private String text;
}
