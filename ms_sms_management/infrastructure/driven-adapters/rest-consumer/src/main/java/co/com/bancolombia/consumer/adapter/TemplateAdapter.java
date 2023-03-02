package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.consumer.RestClient;
import co.com.bancolombia.consumer.adapter.response.Error;
import co.com.bancolombia.consumer.adapter.response.ErrorInalambriaSMS;
import co.com.bancolombia.consumer.adapter.response.ErrorTemplate;
import co.com.bancolombia.consumer.adapter.response.SuccessTemplate;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.TemplateSms;
import co.com.bancolombia.model.message.gateways.TemplateGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.TECHNICAL_EXCEPTION;

@Repository
@RequiredArgsConstructor
public class TemplateAdapter implements TemplateGateway {

    private final ConsumerProperties properties;

    private final RestClient<Alert, SuccessTemplate> client;

    private static final String ID_TEMPLATE = "idTemplate";

    @Override
    public Mono<TemplateSms> findTemplateEmail(Alert pAlert) {
        String endpoint = properties.getResources().getEndpointTemplate();
        return Mono.just(pAlert)
                .flatMap(alert -> client.requestGet(endpoint, createParams(pAlert.getTemplate().getName()),
                        pAlert.getTemplate().getParameters(), SuccessTemplate.class, ErrorTemplate.class))
                .map(response-> TemplateSms.builder().bodyText(response.getData().getPlainText()).build())
                .onErrorMap(Error.class, e -> new TechnicalException(((ErrorTemplate) e.getData()).getError().getReason(),
                        TECHNICAL_EXCEPTION,e.getHttpsStatus()))
                .onErrorMap(e -> new TechnicalException(e.getMessage(),TECHNICAL_EXCEPTION,1));
    }

    public MultiValueMap<String, String> createParams(String nameTemplate) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(ID_TEMPLATE, nameTemplate);
        return params;
    }
}
