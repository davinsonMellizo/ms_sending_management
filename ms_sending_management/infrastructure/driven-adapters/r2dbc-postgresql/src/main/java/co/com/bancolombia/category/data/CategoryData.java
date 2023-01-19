package co.com.bancolombia.category.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("category")
public class CategoryData {
    @Id
    private Integer id;
    private String name;
    private String creationUser;
    private LocalDateTime createdDate;

    @Transient
    private Boolean isNew;

}
