package co.com.bancolombia.newness.data;

import co.com.bancolombia.model.newness.Newness;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NewnessMapper {
    NewnessData toData(Newness newness);
}
