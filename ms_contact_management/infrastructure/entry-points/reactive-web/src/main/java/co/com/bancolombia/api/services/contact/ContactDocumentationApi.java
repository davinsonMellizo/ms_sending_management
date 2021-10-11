package co.com.bancolombia.api.services.contact;

import co.com.bancolombia.api.dto.ContactDTO;
import co.com.bancolombia.api.dto.ResponseContactsDTO;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.error.Error;
import co.com.bancolombia.model.response.StatusResponse;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

public class ContactDocumentationApi {

    private final String TAG = "Contacts";
    private final String ERROR = "Error";
    private final String SUCCESSFUL = "successful";

    protected Consumer<Builder> save() {
        return ops -> ops.tag(TAG)
                .operationId("SaveContact").summary("Save contact")
                .description("save a Client contact").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("Contact to create").required(true).implementation(ContactDTO.class))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(Contact.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> find() {
        return ops -> ops.tag(TAG)
                .operationId("findContacts").summary("Find contacts")
                .description("Find contacts by client").tags(new String[]{TAG})
                .parameter(createHeader(Long.class, "document-number", "Client Document Number"))
                .parameter(createHeader(String.class, "document-type", "Client Document Type"))
                .parameter(headerNoRequired(String.class, "consumer", "Code consumer"))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(ResponseContactsDTO.class))
                .response(responseBuilder().responseCode("400").description("Bad Request").implementation(String.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> update() {
        return ops -> ops.tag(TAG)
                .operationId("updateContact").summary("Update contact")
                .description("Update client contact ").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("Contact to Update").required(true).implementation(ContactDTO.class))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(StatusResponse.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> delete() {
        return ops -> ops.tag(TAG)
                .operationId("deleteContact").summary("Delete contact")
                .description("Delete a client contact").tags(new String[]{TAG})
                .parameter(createHeader(Long.class, "document-number", "Client Document Number"))
                .parameter(createHeader(String.class, "document-type", "Client Document Type"))
                .parameter(createHeader(String.class, "contact-medium", "Type of contact"))
                .parameter(createHeader(String.class, "consumer", "Code Consumer"))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(String.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    private <T> org.springdoc.core.fn.builders.parameter.Builder createHeader(Class<T> clazz, String name, String description) {
        return parameterBuilder().in(HEADER).implementation(clazz).required(true).name(name).description(description);
    }

    private <T> org.springdoc.core.fn.builders.parameter.Builder headerNoRequired(Class<T> clazz, String name, String description) {
        return parameterBuilder().in(HEADER).implementation(clazz).name(name).description(description);
    }
}
