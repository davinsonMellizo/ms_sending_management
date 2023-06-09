package co.com.bancolombia.model.contact;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Contact {
    private Integer id;
    private String segment;
    private String contactWay;
    private String contactWayName;
    private Long documentNumber;
    private String documentType;
    private String value;
    private String stateContact;
    private Integer idState;
    private String environmentType;
    private Boolean previous;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public Contact segment(String segment) {
        this.setSegment(segment);
        return this;
    }

}
