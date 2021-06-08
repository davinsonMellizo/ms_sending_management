package co.com.bancolombia.model.enrollmentcontact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EnrollmentContact {
    private Integer id;
    private String code;
}
