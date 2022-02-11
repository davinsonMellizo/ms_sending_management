package co.com.bancolombia.model.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Category {

    private Integer id;
    private String name;
    private String creationUser;
    private LocalDateTime createdDate;
}
