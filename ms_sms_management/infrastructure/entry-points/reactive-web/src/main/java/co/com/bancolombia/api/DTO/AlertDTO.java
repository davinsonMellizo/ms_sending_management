package co.com.bancolombia.api.DTO;

import co.com.bancolombia.Request;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Template;
import co.com.bancolombia.model.message.To;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AlertDTO extends Request {
    @NotNull
    private String priority;
    private To to;
    private String message;
    private Template template;
    private String url;
    private String provider;
    private String logKey;
    private String category;
    private Boolean isFlash;
    private Boolean isPremium;

    public Mono<Alert> toModel(){
        return  Mono.just(Alert.builder()
                .priority(this.priority)
                .to(this.to)
                .message(this.message)
                .template(this.template)
                .url(this.url)
                .provider(this.provider)
                .logKey(this.logKey)
                .category(this.category)
                .category(this.category)
                .isFlash(this.isFlash)
                .isPremium(this.isPremium)
                .build());
    }


}
