package co.com.bancolombia.consumer.adapter.mapper;


import co.com.bancolombia.Request;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    Request toEntity(RequestForm requestForm);
}
