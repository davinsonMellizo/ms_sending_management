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
                .url(message.getUrl())
                .phone(client.getContacts().getPhone())
                .phoneIndicator(client.getContacts().getPhoneIndicator())
                .mail(client.getContacts().getMail())
                .parameters(message.getParameters())
                .attachments(message.getAttachments())
                .remitter(message.getRemitter())
                .priority(message.getPriority())
                .build());
    }
}
