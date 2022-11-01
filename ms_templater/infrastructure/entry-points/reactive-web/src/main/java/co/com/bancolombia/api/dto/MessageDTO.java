package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.template.dto.MessageRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    @NotNull(message = "Campo obligatorio")
    @Size(min = 1, max = 50, message = "Longitud maxima de 50 caracteres")
    private String idTemplate;

    @NotNull(message = "Campo obligatorio")
    private Map<String, String> messageValues;

    public Mono<MessageRequest> toModel() {
        return Mono.just(MessageRequest.builder()
                .idTemplate(this.idTemplate)
                .messageValues(this.messageValues)
                .build());
    }
}
