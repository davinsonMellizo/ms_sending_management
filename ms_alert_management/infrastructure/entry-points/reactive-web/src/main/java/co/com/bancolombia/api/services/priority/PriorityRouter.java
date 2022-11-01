package co.com.bancolombia.api.services.priority;

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
public class PriorityRouter extends PriorityDocumentationApi {
    private final ApiProperties apiProperties;
    private static final String ID = "/{id}";
    private static final String PROVIDER = "-provider" + ID;

    @Bean
    public RouterFunction<ServerResponse> routerFunctionPriority(PriorityHandler handler) {
        final String url = apiProperties.getPriority();
        return route().POST(url, accept(APPLICATION_JSON), handler::savePriority, save()).build()
                .and(route().PUT(url, accept(APPLICATION_JSON), handler::updatePriority, update()).build())
                .and(route().GET(url + PROVIDER, accept(APPLICATION_JSON), handler::findAllPriorityByProvider,
                        findAll()).build())
                .and(route().GET(url + ID, accept(APPLICATION_JSON), handler::findPriorityById, find()).build())
                .and(route().DELETE(url + ID, accept(APPLICATION_JSON), handler::deletePriorityById,
                        delete()).build());
    }
}
