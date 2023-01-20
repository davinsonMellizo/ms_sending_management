package co.com.bancolombia.model.alert;

import co.com.bancolombia.model.Request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Alert extends Request {
    private Integer operation;
    private Integer documentType;
    private Long documentNumber;
    private List<String> preferences;
    private String alert;
    private String phone;
    private String phoneIndicator;
    private String mail;

    private Map<String, String> parameters;


}
