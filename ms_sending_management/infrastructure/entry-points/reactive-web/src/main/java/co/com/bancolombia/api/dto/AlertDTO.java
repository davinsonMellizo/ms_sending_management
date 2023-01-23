package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.message.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AlertDTO {
    private Boolean retrieveInformation;
    private ClientDTO client;
    private MessageDTO message;
    private DataDTO data;

    public Mono<Message> toModel(){
        return Mono.just(Message.builder()
                .documentNumber(client.getIdentification().getDocumentNumber())
                .documentType(client.getIdentification().getDocumentType())
                .preferences(message.getPreferences())
                .consumer(data.getConsumer())
                .alert(data.getAlert())
                .transactionCode(data.getTransactionCode())
                .amount(data.getAmount())
                .url(message.getSms().getUrl())
                .phone(message.getSms().getPhone())
                .phoneIndicator(message.getSms().getPhoneIndicator())
                .mail(message.getMail().getAddress())
                .parameters(message.getParameters())
                .attachments(message.getMail().getAttachments())
                .remitter(message.getMail().getRemitter())
                .priority(message.getSms().getPriority())
                .build());
    }
}
