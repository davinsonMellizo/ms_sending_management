package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.consumer.Consumer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsumerDTO {

    @Max(value = 999, message = "{constraint.max}")
    @Min(value = 0, message = "{constraint.min}")
    @NotNull(message = "{constraint.not_null}")
    private String id;
    @Size(min = 1, max = 10, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String code;

    public Mono<Consumer> toModel() {
        return Mono.just(Consumer.builder()
                .id(this.id)
                .code(this.code)
                .build());
    }
}
