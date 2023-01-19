package co.com.bancolombia.category;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.category.data.CategoryData;
import co.com.bancolombia.category.data.CategoryMapper;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.category.Category;
import co.com.bancolombia.model.category.gateways.CategoryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.FIND_CATEGORY_BY_ID_ERROR;

@Repository
public class CategoryRepositoryImplement extends AdapterOperations<Category, CategoryData, Integer, CategoryRepository>
        implements CategoryGateway {

    @Autowired
    public CategoryRepositoryImplement(CategoryRepository repository, CategoryMapper mapper) {
        super(repository, null, mapper::toEntity);
    }

    @Override
    public Mono<Category> findCategoryById(Integer id) {
        return doQuery(repository.findById(id))
                .onErrorMap(e -> new TechnicalException(e, FIND_CATEGORY_BY_ID_ERROR));
    }

}
