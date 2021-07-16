package co.com.bancolombia.consumer.data;

import co.com.bancolombia.model.consumer.Consumer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConsumerMapper {

    Consumer toEntity(ConsumerData consumerData);

    @Mapping(target = "isNew", ignore = true)
    ConsumerData toData(Consumer consumer);
}
