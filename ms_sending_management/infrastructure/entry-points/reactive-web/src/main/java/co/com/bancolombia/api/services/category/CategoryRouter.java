package co.com.bancolombia.api.services.category;

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
public class CategoryRouter extends CategoryDocumentationApi {
    private final ApiProperties apiProperties;
    private static String ID = "/{id}";

    @Bean
    public RouterFunction<ServerResponse> routerFunctionCategory(CategoryHandler handler) {
        final String url = "/category";
        return route().POST(url, accept(APPLICATION_JSON), handler::saveCategory, save()).build()
                .and(route().PUT(url, accept(APPLICATION_JSON), handler::updateCategory, update()).build())
                .and(route().GET(url, accept(APPLICATION_JSON), handler::findAllCategory, findAll()).build())
                .and(route().GET(url + ID, accept(APPLICATION_JSON), handler::findCategoryById, find()).build())
                .and(route().DELETE(url + ID, accept(APPLICATION_JSON), handler::deleteCategoryById, delete()).build());
    }
}
