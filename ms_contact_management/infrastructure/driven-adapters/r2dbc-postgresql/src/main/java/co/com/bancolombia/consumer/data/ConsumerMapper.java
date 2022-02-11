package co.com.bancolombia.consumer.data;

import co.com.bancolombia.model.consumer.Consumer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConsumerMapper {

    Consumer toEntity(ConsumerData consumerData);

}
