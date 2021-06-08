package co.com.bancolombia.enrollmentcontact.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("enrollment_contact")
public class EnrollmentContactData {
    @Id
    private String code;
    private Integer id;

}