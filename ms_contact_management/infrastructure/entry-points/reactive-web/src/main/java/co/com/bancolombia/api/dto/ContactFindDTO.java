package co.com.bancolombia.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ContactFindDTO {
    private String segment;
    private String contactMedium;
    private String value;
    private String state;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
