package co.com.bancolombia.model.providerservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProviderService {

    private Integer id;
    private String idProvider;
    private Integer idService;

    private String code;
    private String description;
    private String service;
}
