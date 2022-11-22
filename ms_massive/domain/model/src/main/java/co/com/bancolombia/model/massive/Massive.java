package co.com.bancolombia.model.massive;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Massive {

    private String idCampaign;
    private String idConsumer;
    private Integer numberOfRecords;

}