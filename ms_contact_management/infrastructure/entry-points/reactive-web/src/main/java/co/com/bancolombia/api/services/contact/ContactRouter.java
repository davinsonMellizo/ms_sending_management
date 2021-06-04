package co.com.bancolombia.api.services.contact;

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
public class ContactRouter extends DocumentationApi {
    private final ApiProperties apiProperties;

    @Bean
    public RouterFunction<ServerResponse> routerFunctionContact(ContactHandler contactHandler) {
        return route().POST(apiProperties.getSaveContacts(), accept(APPLICATION_JSON), contactHandler::saveContact, saveContactAPI()).build()
                .and(route().GET(apiProperties.getFindContacts(), accept(APPLICATION_JSON), contactHandler::findContacts, findContactAPI()).build())
                .and(route().PUT(apiProperties.getUpdateContacts(), accept(APPLICATION_JSON), contactHandler::updateContact, updateContactAPI()).build())
                .and(route().DELETE(apiProperties.getDeleteContacts(), accept(APPLICATION_JSON), contactHandler::deleteContact, deleteContactAPI()).build());
    }
}
