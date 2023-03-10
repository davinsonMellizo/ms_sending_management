package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.message.Message;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AlertDTO {
    @NotNull(message = "{constraint.not_null}")
    @Schema(allowableValues = {"true", "false"})
    private Boolean retrieveInformation;
    @ArraySchema(minItems = 1)
    @Size(min = 1)
    @NotNull(message = "{constraint.not_null}")
    private @Valid List<RecipientDTO> recipients;
    @NotNull(message = "{constraint.not_null}")
    private @Valid MessageDTO message;
    @Builder.Default
    private @Valid AlertParametersDTO alertParameters = new AlertParametersDTO();
    @Builder.Default
    private String trackID = "";

    public Mono<List<Message>> toModel() {
        return Flux.fromIterable(recipients)
                .map(recipient -> Message.builder()
                        .category(message.getCategory())
                        .documentNumber(recipient.getIdentification().getDocumentNumber())
                        .documentType(recipient.getIdentification().getDocumentType())
                        .preferences(message.getPreferences())
                        .consumer(alertParameters.getConsumer())
                        .alert(alertParameters.getAlert())
                        .transactionCode(alertParameters.getTransactionCode())
                        .amount(alertParameters.getAmount())
                        .url(recipient.getContacts().getSms().getUrlForShortening())
                        .phone(recipient.getContacts().getSms().getPhoneNumber())
                        .phoneIndicator(recipient.getContacts().getSms().getPhoneIndicator())
                        .mail(recipient.getContacts().getMail().getAddress())
                        .parameters(message.getParameters())
                        .attachments(recipient.getContacts().getMail().getAttachments())
                        .remitter(recipient.getContacts().getMail().getRemitter())
                        .priority(message.getPriority())
                        .retrieveInformation(retrieveInformation)
                        .applicationCode(recipient.getContacts().getPush().getApplicationCode())
                        .template(message.getTemplate())
                        .build()).collectList();
    }
}
