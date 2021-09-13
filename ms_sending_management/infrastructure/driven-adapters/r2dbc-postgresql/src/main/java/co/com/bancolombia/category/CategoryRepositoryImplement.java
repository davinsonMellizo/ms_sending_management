package co.com.bancolombia.category;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.category.data.CategoryData;
import co.com.bancolombia.category.data.CategoryMapper;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.category.Category;
import co.com.bancolombia.model.category.gateways.CategoryGateway;
import co.com.bancolombia.model.response.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.*;

@Repository
public class CategoryRepositoryImplement extends AdapterOperations<Category, CategoryData, Integer, CategoryRepository>
        implements CategoryGateway {

    @Autowired
    private TimeFactory timeFactory;

    @Autowired
    public CategoryRepositoryImplement(CategoryRepository repository, CategoryMapper mapper) {
        super(repository, mapper::toData, mapper::toEntity);
    }

    @Override
    public Mono<List<Category>> findAll() {
        return repository.findAll()
                .map(this::convertToEntity)
                .collectList()
                .onErrorMap(e -> new TechnicalException(e, FIND_ALL_CATEGORIES_ERROR));
    }

    @Override
    public Mono<Category> findCategoryById(Integer id) {
        return doQuery(repository.findById(id))
                .onErrorMap(e -> new TechnicalException(e, FIND_CATEGORY_BY_ID_ERROR));
    }

    @Override
    public Mono<Category> saveCategory(Category category) {
        return Mono.just(category)
                .map(this::convertToData)
                .map(categoryData -> categoryData.toBuilder()
                        .isNew(true)
                        .createdDate(timeFactory.now())
                        .build())
                .flatMap(categoryData -> repository.save(categoryData))
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, SAVE_CATEGORY_ERROR));
    }

    @Override
    public Mono<StatusResponse<Category>> updateCategory(Category category) {
        return this.findCategoryById(category.getId())
                .map(category1 -> StatusResponse.<Category>builder()
                        .before(category1)
                        .actual(category)
                        .build())
                .flatMap(this::update);
    }

    private Mono<StatusResponse<Category>> update(StatusResponse<Category> response) {
        return Mono.just(response.getActual())
                .map(this::convertToData)
                .map(data -> data.toBuilder().isNew(false).createdDate(response.getBefore().getCreatedDate()).build())
                .flatMap(repository::save)
                .map(this::convertToEntity)
                .map(actual -> response.toBuilder().actual(actual).description("Actualización Exitosa").build())
                .onErrorMap(e -> new TechnicalException(e, UPDATE_CATEGORY_ERROR));
    }

    @Override
    public Mono<Integer> deleteCategoryById(Integer id) {
        return repository.deleteById(id)
                .onErrorMap(e -> new TechnicalException(e, DELETE_CATEGORY_ERROR))
                .thenReturn(id);
    }
}
