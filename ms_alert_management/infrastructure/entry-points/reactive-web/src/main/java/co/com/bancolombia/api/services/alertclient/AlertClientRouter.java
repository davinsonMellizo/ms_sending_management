package co.com.bancolombia.api.services.alertclient;

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
public class AlertClientRouter extends AlertClientDocumentationApi {
    private final ApiProperties apiProperties;
    private static final String RELATION = "-client";
    private static final String ID = "/{id}";
    private static final String KIT = "/basic-kit";

    @Bean
    public RouterFunction<ServerResponse> routerFunctionAlertClient(AlertClientHandler handler) {
        final String url = apiProperties.getAlert().concat(RELATION);
        return route().POST(url, accept(APPLICATION_JSON), handler::saveAlertClient, save()).build()
                .and(route().POST(url+KIT, accept(APPLICATION_JSON), handler::basicKit, basicKit()).build())
                .and(route().GET(url, accept(APPLICATION_JSON), handler::findAlertClientByClient, find()).build())
                .and(route().PUT(url, accept(APPLICATION_JSON), handler::updateAlertClient, update()).build())
                .and(route().DELETE(url, accept(APPLICATION_JSON), handler::deleteAlertClient, delete()).build());
    }
}
