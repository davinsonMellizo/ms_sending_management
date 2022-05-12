package co.com.bancolombia.api.services.client;

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
public class ClientRouter extends ClientDocumentationApi {
    private final ApiProperties apiProperties;
    private final static String INACTIVE = "/inactive";
    private final static String DELETE = "/delete-range";

    @Bean
    public RouterFunction<ServerResponse> routerFunctionClient(ClientHandler clientHandler) {
        final String url = apiProperties.getClient();
        return route().POST(url, accept(APPLICATION_JSON), clientHandler::saveClient, save()).build()
                .and(route().PUT(url, accept(APPLICATION_JSON), clientHandler::updateClient, update()).build())
                .and(route().PUT(url + INACTIVE, accept(APPLICATION_JSON), clientHandler::inactivateClient,
                        inactive()).build())
                .and(route().DELETE(url + DELETE, accept(APPLICATION_JSON),
                        clientHandler::deleteClient, find()).build());
    }
}
