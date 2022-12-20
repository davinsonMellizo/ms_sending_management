package co.com.bancolombia.api.services.campaign;

import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.error.Error;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

public class CampaignDocumentationApi {

    private static final String TAG = "Campaign";
    private static final String ERROR = "Error";
    private static final String SUCCESSFUL = "successful";

    protected Consumer<Builder> save() {
        return ops -> ops.tag(TAG)
                .operationId("SaveCampaign").summary("Save Campaign")
                .description("Create new Campaign").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("Campaign to create").required(true)
                        .implementation(Campaign.class))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL)
                        .implementation(Campaign.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> find() {
        return ops -> ops.tag(TAG)
                .operationId("findCampaign").summary("Find Campaign")
                .description("Find Campaign by id").tags(new String[]{TAG})
                .parameter(createHeader(String.class, "id", "Campaign identifier"))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL)
                        .implementation(Campaign.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> findAll() {
        return ops -> ops.tag(TAG)
                .operationId("findAllCampaign").summary("Find all Campaign")
                .description("Find all Campaign by id").tags(new String[]{TAG})
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL)
                        .implementationArray(Campaign.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> update() {
        return ops -> ops.tag(TAG)
                .operationId("updateCampaign").summary("Update Campaign")
                .description("Update Campaign by id").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("Campaign to Update").required(true)
                        .implementation(Campaign.class))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL)
                        .implementation(Campaign.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> delete() {
        return ops -> ops.tag(TAG)
                .operationId("deleteCampaign").summary("Delete Campaign")
                .description("Delete a Campaign by id").tags(new String[]{TAG})
                .parameter(createHeader(String.class, "id", "Campaign identifier"))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(String.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }


    protected Consumer<Builder> deleteTrigger() {
        return ops -> ops.tag(TAG)
                .operationId("deleteTrigger").summary("Delete Trigger")
                .description("Delete a Trigger by Name").tags(new String[]{TAG})
                .parameter(createHeader(String.class, "name", "Trigger identifier"))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(String.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    private <T> org.springdoc.core.fn.builders.parameter.Builder createHeader(Class<T> clazz, String name, String description) {
        return parameterBuilder().in(PATH).implementation(clazz).required(true).name(name).description(description);
    }
}
