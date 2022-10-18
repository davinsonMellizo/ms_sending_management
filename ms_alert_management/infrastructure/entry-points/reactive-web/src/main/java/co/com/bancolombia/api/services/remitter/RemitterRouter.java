package co.com.bancolombia.api.services.remitter;

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
public class RemitterRouter extends RemitterDocumentationApi {
    private final ApiProperties apiProperties;
    private static final String ID = "/{id}";

    @Bean
    public RouterFunction<ServerResponse> routerFunctionRemitter(RemitterHandler handler) {
        final String url = apiProperties.getRemitter();
        return route().POST(url, accept(APPLICATION_JSON), handler::saveRemitter, save()).build()
                .and(route().PUT(url, accept(APPLICATION_JSON), handler::updateRemitter, update()).build())
                .and(route().GET(url, accept(APPLICATION_JSON), handler::findAllRemitter, findAll()).build())
                .and(route().GET(url + ID, accept(APPLICATION_JSON), handler::findRemitter, find()).build())
                .and(route().DELETE(url + ID, accept(APPLICATION_JSON), handler::deleteRemitter, delete()).build());
    }
}
