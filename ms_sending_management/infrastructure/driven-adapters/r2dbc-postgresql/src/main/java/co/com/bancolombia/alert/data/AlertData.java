package co.com.bancolombia.alert.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("alert")
public class AlertData {

    @Id
    private String id;
    private String idTransaction;
    private String idConsumer;
    private Integer idCategory;
    private String description;
    private String providerMail;
    private String providerSms;
    private String templateName;
    private String remitter;
    private Integer idState;
    private Integer priority;
    private String nature;
    private String message;
    private String push;
    private Boolean obligatory;
    private Boolean basicKit;

    @Transient
    private Boolean isNew;

}