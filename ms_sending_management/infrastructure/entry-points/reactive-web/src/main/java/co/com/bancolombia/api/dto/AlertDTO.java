package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.message.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AlertDTO {
    @NotNull(message = "{constraint.not_null}")
    @Schema(allowableValues = {"true", "false"})
    private Boolean retrieveInformation;
    @Builder.Default
    private @Valid ClientDTO client = new ClientDTO();
    private @Valid MessageDTO message;
    private @Valid AlertParametersDTO alertParameters;

    public Mono<Message> toModel(){
        return Mono.just(Message.builder()
                .documentNumber(client.getIdentification().getDocumentNumber())
                .documentType(client.getIdentification().getDocumentType())
                .preferences(message.getPreferences())
                .consumer(alertParameters.getConsumer())
                .alert(alertParameters.getAlert())
                .transactionCode(alertParameters.getTransactionCode())
                .amount(alertParameters.getAmount())
                .url(message.getSms().getUrl())
                .phone(message.getSms().getPhone())
                .phoneIndicator(message.getSms().getPhoneIndicator())
                .mail(message.getMail().getAddress())
                .parameters(message.getParameters())
                .attachments(message.getMail().getAttachments())
                .remitter(message.getMail().getRemitter())
                .priority(message.getSms().getPriority())
                .retrieveInformation(retrieveInformation)
                .applicationCode(message.getPush().getApplicationCode())
                .template(message.getMail().getTemplate())
                .build());
    }
}
