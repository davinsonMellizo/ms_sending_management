package co.com.bancolombia.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;


@Configuration
public class RouterLog extends Documentation{

    @Bean
    public RouterFunction<ServerResponse> routerFunctionLogs(HandlerLog handler) {
        return route().GET("/list", accept(APPLICATION_JSON), handler::listLogs, find()).build();

    }
}
