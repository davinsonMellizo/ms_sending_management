package co.com.bancolombia.api.dto;

import co.com.bancolombia.Request;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Template;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import reactor.core.publisher.Mono;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AlertDTO extends Request {
    @Schema(description = "Obligatorio para envío de sms "
            , allowableValues = {"0","1","2","3", "4", "5"})
    @Max(value = 5, message = "{constraint.max}")
    @Min(value = 0, message = "{constraint.min}")
    @NotNull(message = "{constraint.not_null}")
    @NotBlank(message ="{constraint.not_blank}" )
    private String priority;
    @NotNull(message = "{constraint.not_null}")
    private  @Valid Alert.To to;
    @Builder.Default
    private String message="";
    @Builder.Default
    private Template template =new Template();
    @Builder.Default
    private String urlForShortening = "";
    @NotNull(message = "{constraint.not_null}")
    @NotBlank(message ="{constraint.not_blank}" )
    private String provider;
    @NotNull(message = "{constraint.not_null}")
    @NotBlank(message ="{constraint.not_blank}" )
    private String logKey;
    @Builder.Default
    private String category="";
    @Builder.Default
    private Boolean isFlash=false;
    @Builder.Default
    private Boolean isPremium= false;

    public Mono<Alert> toModel(){
        return  Mono.just(Alert.builder()
                .priority(this.priority)
                .to(this.to)
                .message(this.message)
                .template(this.template)
                .urlForShortening(this.urlForShortening)
                .provider(this.provider)
                .logKey(this.logKey)
                .category(this.category)
                .category(this.category)
                .isFlash(this.isFlash)
                .isPremium(this.isPremium)
                .build());
    }



}
