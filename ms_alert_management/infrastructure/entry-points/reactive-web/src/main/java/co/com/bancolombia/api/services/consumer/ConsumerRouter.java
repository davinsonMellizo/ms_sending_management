package co.com.bancolombia.api.services.consumer;

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
public class ConsumerRouter extends ConsumerDocumentationApi {

    private final ApiProperties apiProperties;
    private static String ID = "/{id}";

    @Bean
    public RouterFunction<ServerResponse> routerFunctionConsumer(ConsumerHandler handler) {
        final String url = apiProperties.getConsumer();
        return route().POST(url, accept(APPLICATION_JSON), handler::saveConsumer, save()).build()
                .and(route().PUT(url, accept(APPLICATION_JSON), handler::updateConsumer, update()).build())
                .and(route().GET(url, accept(APPLICATION_JSON), handler::findAllConsumer, findAll()).build())
                .and(route().GET(url + ID, accept(APPLICATION_JSON), handler::findConsumer, find()).build())
                .and(route().DELETE(url + ID, accept(APPLICATION_JSON), handler::deleteConsumer, delete()).build());
    }
}
