package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.consumer.Consumer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsumerDTO extends DTO<Consumer> {

    @Size(min = 1, max = 10, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String id;
    @Size(min = 1, max = 50, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String description;
    @Size(min = 1, max = 10, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String segment;

    @Override
    public Mono<Consumer> toModel() {
        return Mono.just(Consumer.builder()
                .id(this.id)
                .description(this.description)
                .segment(this.segment)
                .build());
    }
}
