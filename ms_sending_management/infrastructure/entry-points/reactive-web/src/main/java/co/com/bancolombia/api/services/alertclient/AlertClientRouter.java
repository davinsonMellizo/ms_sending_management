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
    private final static String RELATION = "-client";

    @Bean
    public RouterFunction<ServerResponse> routerFunctionAlertClient(AlertClientHandler handler) {
        final String url = apiProperties.getAlert().concat(RELATION);
        return route().POST(url, accept(APPLICATION_JSON), handler::saveAlertClient, save()).build()
                .and(route().PUT(url, accept(APPLICATION_JSON), handler::updateAlertClient, update()).build())
                .and(route().GET(url, accept(APPLICATION_JSON), handler::findAllAlertClient, find()).build())
                .and(route().DELETE(url, accept(APPLICATION_JSON), handler::deleteAlertClient, delete()).build());
    }
}
