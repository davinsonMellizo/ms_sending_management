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

    @Bean
    public RouterFunction<ServerResponse> routerFunctionClient(ClientHandler clientHandler) {
        return route().POST(apiProperties.getSaveClient(), accept(APPLICATION_JSON), clientHandler::saveClient, saveClientAPI()).build()
                .and(route().GET(apiProperties.getFindClient(), accept(APPLICATION_JSON), clientHandler::findClient, findClientAPI()).build())
                .and(route().PUT(apiProperties.getUpdateClient(), accept(APPLICATION_JSON), clientHandler::updateClient, updateClientAPI()).build())
                .and(route().DELETE(apiProperties.getDeleteClient(), accept(APPLICATION_JSON), clientHandler::deleteClient, deleteClientAPI()).build());
    }
}
