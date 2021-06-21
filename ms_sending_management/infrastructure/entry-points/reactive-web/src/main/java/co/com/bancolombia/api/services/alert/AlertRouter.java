package co.com.bancolombia.api.services.alert;

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
public class AlertRouter extends AlertDocumentationApi {
    private final ApiProperties apiProperties;
    private final static String ID = "/{id}";

    @Bean
    public RouterFunction<ServerResponse> routerFunctionAlert(AlertHandler handler) {
        return route().POST(apiProperties.getAlert(), accept(APPLICATION_JSON), handler::saveAlert, saveAlertAPI()).build()
                .and(route().PUT(apiProperties.getAlert(), accept(APPLICATION_JSON), handler::updateAlert, updateAlertAPI()).build())
                .and(route().GET(apiProperties.getAlert() + ID, accept(APPLICATION_JSON), handler::findAlert, findAlertAPI()).build())
                .and(route().DELETE(apiProperties.getAlert() + ID, accept(APPLICATION_JSON), handler::deleteAlert, deleteAlertAPI()).build());
    }
}
