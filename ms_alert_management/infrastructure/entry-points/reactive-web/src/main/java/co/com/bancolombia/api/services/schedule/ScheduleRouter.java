package co.com.bancolombia.api.services.schedule;

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
public class ScheduleRouter extends ScheduleDocumentationApi {

    private final ApiProperties apiProperties;
    private static final String ID = "/{id}";

    @Bean
    public RouterFunction<ServerResponse> routerFunctionSchedule(ScheduleHandler handler) {
        final String url = apiProperties.getSchedule();
        return route().POST(url, accept(APPLICATION_JSON), handler::saveSchedule, save()).build()
                .and(route().GET(url + ID, accept(APPLICATION_JSON), handler::findScheduleById, find()).build())
                .and(route().PUT(url + ID, accept(APPLICATION_JSON), handler::updateSchedule, update()).build());
    }
}
