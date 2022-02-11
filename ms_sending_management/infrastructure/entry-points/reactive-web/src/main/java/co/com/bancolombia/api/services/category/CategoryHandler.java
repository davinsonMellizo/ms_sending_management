package co.com.bancolombia.api.services.category;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.commons.util.ParamsUtil;
import co.com.bancolombia.api.commons.util.ResponseUtil;
import co.com.bancolombia.api.dto.CategoryDTO;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.usecase.category.CategoryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;

@Component
@RequiredArgsConstructor
public class CategoryHandler {
    private final CategoryUseCase useCase;
    private final ValidatorHandler validatorHandler;

    public Mono<ServerResponse> findCategoryById(ServerRequest serverRequest) {
        return ParamsUtil.getId(serverRequest)
                .map(Integer::parseInt)
                .flatMap(useCase::findCategoryById)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> findAllCategory(ServerRequest serverRequest) {
        return useCase.findAllCategories()
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> saveCategory(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CategoryDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(CategoryDTO::toModel)
                .flatMap(useCase::saveCategory)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> updateCategory(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CategoryDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(CategoryDTO::toModel)
                .flatMap(useCase::updateCategory)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> deleteCategoryById(ServerRequest serverRequest) {
        return ParamsUtil.getId(serverRequest)
                .map(Integer::parseInt)
                .flatMap(useCase::deleteCategoryById)
                .flatMap(ResponseUtil::responseOk);
    }

}
