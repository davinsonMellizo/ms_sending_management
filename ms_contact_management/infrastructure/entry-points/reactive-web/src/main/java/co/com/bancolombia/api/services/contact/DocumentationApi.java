package co.com.bancolombia.api.services.contact;

import co.com.bancolombia.api.dto.Contact;
import co.com.bancolombia.model.error.Error;
import co.com.bancolombia.model.response.ContactsResponse;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

public class DocumentationApi {

    private final String tag = "Contacts";

    protected Consumer<Builder> saveContactAPI() {
        return ops -> ops.tag(tag)
                .operationId("SaveContact").summary("Save contact")
                .description("save a Client contact").tags(new String[]{tag})
                .requestBody(requestBodyBuilder().description("Contact to create").required(true).implementation(Contact.class))
                .response(responseBuilder().responseCode("200").description("successful").implementation(Contact.class))
                .response(responseBuilder().responseCode("500").description("Error").implementation(Error.class));
    }

    protected Consumer<Builder> findContactAPI() {
        return ops -> ops.tag(tag)
                .operationId("findContacts").summary("Find contacts")
                .description("Find contacts by client").tags(new String[]{tag})
                .parameter(createHeader(Long.class, "document-number", "Client Document Number"))
                .parameter(createHeader(Integer.class, "document-type", "Client Document Type"))
                .response(responseBuilder().responseCode("200").description("successful").implementation(ContactsResponse.class))
                .response(responseBuilder().responseCode("400").description("Bad Request").implementation(String.class))
                .response(responseBuilder().responseCode("500").description("Error").implementation(Error.class));
    }

    protected Consumer<Builder> updateContactAPI() {
        return ops -> ops.tag(tag)
                .operationId("updateContact").summary("Update contact")
                .description("Update client contact ").tags(new String[]{tag})
                .requestBody(requestBodyBuilder().description("Contact to Update").required(true).implementation(String.class))
                .response(responseBuilder().responseCode("200").description("successful").implementation(ContactsResponse.class))
                .response(responseBuilder().responseCode("500").description("Error").implementation(Error.class));
    }

    protected Consumer<Builder> deleteContactAPI() {
        return ops -> ops.tag(tag)
                .operationId("deleteContact").summary("Delete contact")
                .description("Delete a client contact").tags(new String[]{tag})
                .parameter(createHeader(Long.class, "document-number", "Client Document Number"))
                .parameter(createHeader(Integer.class, "document-type", "Client Document Type"))
                .parameter(createHeader(String.class,"contact-medium", "Type of contact"))
                .parameter(createHeader(String.class, "enrollment-contact", "Origin of enrollment"))
                .response(responseBuilder().responseCode("200").description("successful").implementation(String.class))
                .response(responseBuilder().responseCode("500").description("Error").implementation(Error.class));
    }
    private <T> org.springdoc.core.fn.builders.parameter.Builder createHeader(Class<T> clazz, String name, String description) {
        return  parameterBuilder().in(HEADER).implementation(clazz).required(true).name(name).description(description);
    }
}
