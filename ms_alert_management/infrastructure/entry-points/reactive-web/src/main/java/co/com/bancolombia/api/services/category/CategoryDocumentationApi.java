package co.com.bancolombia.api.services.category;

import co.com.bancolombia.api.dto.CategoryDTO;
import co.com.bancolombia.model.category.Category;
import co.com.bancolombia.model.error.Error;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

public class CategoryDocumentationApi {

    private static final String TAG = "Category";
    private static final String ERROR = "Error";
    private static final String SUCCESSFUL = "successful";

    protected Consumer<Builder> save() {
        return ops -> ops.tag(TAG)
                .operationId("SaveCategory").summary("Save category")
                .description("Create new category").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("category to create")
                        .required(true).implementation(CategoryDTO.class))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(Category.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> find() {
        return ops -> ops.tag(TAG)
                .operationId("findCategory").summary("Find category")
                .description("Find category by id").tags(new String[]{TAG})
                .parameter(createHeader(String.class, "id", "category identifier"))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(Category.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> findAll() {
        return ops -> ops.tag(TAG)
                .operationId("findCategories").summary("Find all Categories")
                .description("Find all categories").tags(new String[]{TAG})
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL)
                        .implementationArray(Category.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> update() {
        return ops -> ops.tag(TAG)
                .operationId("updateCategory").summary("Update category")
                .description("Update category by id").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("category to Update").required(true)
                        .implementation(CategoryDTO.class))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(Category.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> delete() {
        return ops -> ops.tag(TAG)
                .operationId("deleteCategory").summary("Delete category")
                .description("Delete a category by id").tags(new String[]{TAG})
                .parameter(createHeader(String.class, "id", "category identifier"))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(String.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    private <T> org.springdoc.core.fn.builders.parameter.Builder createHeader(Class<T> clazz, String name,
                                                                              String description) {
        return parameterBuilder().in(PATH).implementation(clazz)
                .required(true).name(name).description(description);
    }
}
