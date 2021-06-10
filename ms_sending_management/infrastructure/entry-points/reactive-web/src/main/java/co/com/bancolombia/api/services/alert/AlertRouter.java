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
public class AlertRouter extends DocumentationApi {
    private final ApiProperties apiProperties;

    @Bean
    public RouterFunction<ServerResponse> routerFunctionAlert(AlertHandler alertHandler) {
        return route().POST(apiProperties.getSaveAlert(), accept(APPLICATION_JSON), alertHandler::saveAlert, saveAlertAPI()).build()
                .and(route().GET(apiProperties.getFindAlert(), accept(APPLICATION_JSON), alertHandler::findAlert, findAlertAPI()).build())
                .and(route().PUT(apiProperties.getUpdateAlert(), accept(APPLICATION_JSON), alertHandler::updateAlert, updateAlertAPI()).build())
                .and(route().DELETE(apiProperties.getDeleteAlert(), accept(APPLICATION_JSON), alertHandler::deleteAlert, deleteAlertAPI()).build());
    }
}
