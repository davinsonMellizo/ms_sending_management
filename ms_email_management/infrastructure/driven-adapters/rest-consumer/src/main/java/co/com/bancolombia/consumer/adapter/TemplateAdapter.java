package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.consumer.RestClient;
import co.com.bancolombia.consumer.adapter.response.ErrorTemplate;
import co.com.bancolombia.consumer.adapter.response.SuccessTemplate;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.TemplateEmail;
import co.com.bancolombia.model.message.gateways.TemplateGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;


@Repository
@RequiredArgsConstructor
public class TemplateAdapter implements TemplateGateway {

    private final ConsumerProperties properties;
    private final RestClient<Alert, SuccessTemplate> client;
    private final String ID_TEMPLATE = "idTemplate";

    @Override
    public Mono<TemplateEmail> findTemplateEmail(Alert pAlert) {

        String endpoint = properties.getResources().getEndpointTemplate();

        return Mono.just(pAlert)
                .filter(alert -> alert.getMessage() == null && alert.getTemplate() != null)
                .flatMap(alert -> client.requestGet(endpoint, createParams(pAlert.getTemplate().getName()),
                        pAlert.getTemplate().getParameters(), SuccessTemplate.class, ErrorTemplate.class))
                .map(response -> TemplateEmail.builder().name(response.getData().getIdTemplate())
                        .bodyHtml(response.getData().getMessageBody()).subject(response.getData().getMessageSubject())
                        .build())
                .onErrorResume(e -> Mono.error(new RuntimeException(e.getMessage())));
    }

    public MultiValueMap<String, String> createParams(String nameTemplate) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(ID_TEMPLATE, nameTemplate);
        return params;
    }


}
