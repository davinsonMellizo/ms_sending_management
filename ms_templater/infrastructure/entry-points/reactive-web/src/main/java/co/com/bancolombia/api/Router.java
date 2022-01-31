package co.com.bancolombia.api;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
@RequiredArgsConstructor
public class Router {

    private final ApiProperties apiProperties;

    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return (route(POST(apiProperties.getCreateTemplate()).and(accept(MediaType.APPLICATION_JSON)),
                handler::createTemplate)
                .andRoute(GET(apiProperties.getGetTemplate()).and(accept(MediaType.APPLICATION_JSON)),
                        handler::getTemplate)
                .andRoute(PUT(apiProperties.getPutTemplate()).and(accept(MediaType.APPLICATION_JSON)),
                        handler::updateTemplate)
//                .andRoute(PUT(apiProperties.getDeleteTemplate()).and(accept(MediaType.APPLICATION_JSON)),
//                        handler::deleteTemplate)
        );
    }
}