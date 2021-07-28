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
    public String consumer;
    public String contactMedium;
    public String value;
    public String state;
    public LocalDateTime createdDate;
    public LocalDateTime modifiedDate;
}
