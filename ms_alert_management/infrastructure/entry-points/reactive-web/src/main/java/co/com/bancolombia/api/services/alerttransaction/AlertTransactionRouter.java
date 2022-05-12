package co.com.bancolombia.api.services.alerttransaction;

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
public class AlertTransactionRouter extends AlertTransactionDocumentationApi {
    private final ApiProperties apiProperties;
    private final static String RELATION = "-transaction";
    private static String ID = "/{id}";

    @Bean
    public RouterFunction<ServerResponse> routerFunctionAlertTransaction(AlertTransactionHandler handler) {
        final String url = apiProperties.getAlert() + RELATION;
        return route().POST(url, accept(APPLICATION_JSON), handler::saveAlertTransaction, save()).build()
                .and(route().GET(url + ID, accept(APPLICATION_JSON), handler::findAllAlertTransaction, find()).build())
                .and(route().DELETE(url, accept(APPLICATION_JSON), handler::deleteAlertTransaction, delete()).build());
    }
}
