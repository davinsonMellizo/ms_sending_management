package co.com.bancolombia.api.services.contact;

import co.com.bancolombia.api.dto.Contact;
import co.com.bancolombia.model.response.ContactsResponse;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

public class DocumentationApi {

    protected Consumer<Builder> saveContactAPI() {
        return ops -> ops.tag("Contact")
                .operationId("findContacts").summary("Find contacts")
                .description("Find contacts by client").tags(new String[]{"Contact"})
                .parameter(parameterBuilder().in(ParameterIn.HEADER).required(true).name("document-number").description("Client Document Number"))
                .parameter(parameterBuilder().in(ParameterIn.HEADER).required(true).name("document-type").description("Client Document Type"))
                .requestBody(requestBodyBuilder().description("Contact to Update").required(true).implementation(Contact.class))
                .response(responseBuilder().responseCode("200").description("successful response").implementation(Contact.class))
                .response(responseBuilder().responseCode("500").description("Error").implementation(String.class));
    }

    protected Consumer<Builder> findContactAPI() {
        return ops -> ops.tag("Contact")
                .operationId("findContacts").summary("Find contacts")
                .description("Find contacts by client").tags(new String[]{"Contact"})
                .parameter(parameterBuilder().in(ParameterIn.HEADER).implementation(Long.class).required(true).name("document-number").description("Client Document Number"))
                .parameter(parameterBuilder().in(ParameterIn.HEADER).implementation(Integer.class).required(true).name("document-type").description("Client Document Type"))
                .response(responseBuilder().responseCode("200").description("successful response").implementation(ContactsResponse.class))
                .response(responseBuilder().responseCode("400").description("Bad Request").implementation(String.class))
                .response(responseBuilder().responseCode("500").description("Error").implementation(String.class));
    }

    protected Consumer<Builder> updateContactAPI() {
        return ops -> ops.tag("Contact")
                .operationId("findContacts").summary("Find contacts")
                .description("Find contacts by client").tags(new String[]{"Contact"})
                .parameter(parameterBuilder().in(ParameterIn.HEADER).required(true).name("document-number").description("Client Document Number"))
                .parameter(parameterBuilder().in(ParameterIn.HEADER).required(true).name("document-type").description("Client Document Type"))
                .requestBody(requestBodyBuilder().description("Contact to Update").required(true).implementation(String.class))
                .response(responseBuilder().responseCode("200").description("successful response").implementation(String.class))
                .response(responseBuilder().responseCode("500").description("Error").implementation(String.class));
    }

    protected Consumer<Builder> deleteContactAPI() {
        return ops -> ops.tag("Contact")
                .operationId("findContacts").summary("Find contacts")
                .description("Find contacts by client").tags(new String[]{"Contact"})
                .parameter(parameterBuilder().in(ParameterIn.HEADER).required(true).name("document-number").description("Client Document Number"))
                .parameter(parameterBuilder().in(ParameterIn.HEADER).required(true).name("document-type").description("Client Document Type"))
                .response(responseBuilder().responseCode("200").description("successful response").implementation(String.class))
                .response(responseBuilder().responseCode("500").description("Error").implementation(String.class));
    }
}
