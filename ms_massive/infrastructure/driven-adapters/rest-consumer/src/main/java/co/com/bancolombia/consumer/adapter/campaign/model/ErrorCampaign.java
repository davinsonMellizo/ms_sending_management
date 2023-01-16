package co.com.bancolombia.consumer.adapter.campaign.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ErrorCampaign {

    private String reason;
    private String domain;
    private String code;
    private String message;

}
