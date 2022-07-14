package co.com.bancolombia.model.template.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTemplateResponse {
    private TemplateResponse before;
    private TemplateResponse current;
}
