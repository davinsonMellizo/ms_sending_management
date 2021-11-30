package co.com.bancolombia.api.services.sendalert.dto;

import co.com.bancolombia.model.message.Attachment;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MessageDTO {
    private Integer operation;
    private Integer documentType;
    private Long documentNumber;
    private String consumer;
    private String alert;
    private String transactionCode;
    private Long amount;
    private String url;

    private String phone;
    @Builder.Default
    private String phoneIndicator = "";

    private String mail;

    private List<Parameter> parameters;
    private ArrayList<Attachment> attachments;

    public Mono<Message> toModel(){
        return Mono.just(Message.builder()
                .operation(this.operation)
                .documentNumber(this.documentNumber)
                .documentType(this.documentType)
                .consumer(this.consumer)
                .alert(this.alert)
                .transactionCode(this.transactionCode)
                .amount(this.amount)
                .url(this.url)
                .phone(this.phone)
                .phoneIndicator(this.phoneIndicator)
                .mail(this.mail)
                .parameters(this.parameters)
                .attachments(this.attachments)
                .build());
    }
}
