package co.com.bancolombia.category.data;

import co.com.bancolombia.model.category.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryData categoryData);

}
