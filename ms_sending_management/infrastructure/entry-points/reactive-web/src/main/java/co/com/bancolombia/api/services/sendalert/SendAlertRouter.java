package co.com.bancolombia.api.services.sendalert;

import co.com.bancolombia.api.ApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;


@Configuration
@RequiredArgsConstructor
public class SendAlertRouter extends SendAlertDocumentationApi {
    private final ApiProperties apiProperties;

    @Bean
    public RouterFunction<ServerResponse> routerFunctionSendAlert(SendAlertHandler handler) {
        final String urlClient = apiProperties.getSend()+"-client";
        final String urlContacts = apiProperties.getSend()+"-contacts";
        return route().POST(urlClient, accept(APPLICATION_JSON), handler::sendAlertByClient, send()).build()
                .and(route().POST(urlContacts, accept(APPLICATION_JSON), handler::sendAlertByContacts, send()).build());
    }
}
