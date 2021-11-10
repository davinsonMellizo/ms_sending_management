package co.com.bancolombia.prefix;


import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.model.prefix.Prefix;
import co.com.bancolombia.model.prefix.gateways.PrefixRepository;
import co.com.bancolombia.prefix.data.PrefixData;
import co.com.bancolombia.prefix.data.PrefixMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public class IPrefixRepositoryImplement
        extends AdapterOperations<Prefix, PrefixData, String, IPrefixRepository>
        implements PrefixRepository {


    public IPrefixRepositoryImplement(IPrefixRepository repository, PrefixMapper mapper) {
        super(repository, null, mapper::toEntity);
    }

    @Override
    public Mono<Prefix> findPrefix(String code) {
        return repository.findById(code)
                .map(this::convertToEntity);
    }
}
