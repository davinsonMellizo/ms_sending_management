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
        final String url = apiProperties.getAlert();
        return route().POST(url, accept(APPLICATION_JSON), handler::saveAlert, save()).build()
                .and(route().PUT(url, accept(APPLICATION_JSON), handler::updateAlert, update()).build())
                .and(route().GET(url + ID, accept(APPLICATION_JSON), handler::findAlert, find()).build())
                .and(route().DELETE(url + ID, accept(APPLICATION_JSON), handler::deleteAlert, delete()).build());
    }
}
