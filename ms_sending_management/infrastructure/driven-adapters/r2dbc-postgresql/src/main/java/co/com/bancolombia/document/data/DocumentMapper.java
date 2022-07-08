package co.com.bancolombia.document.data;

import co.com.bancolombia.model.document.Document;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    Document toEntity(DocumentData documentData);

}
