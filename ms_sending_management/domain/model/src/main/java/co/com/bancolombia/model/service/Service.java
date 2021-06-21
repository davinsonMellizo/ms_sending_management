package co.com.bancolombia.model.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Service {
    private Integer id;
    private String name;
    private String creationUser;
    private LocalDateTime createdDate;
}
