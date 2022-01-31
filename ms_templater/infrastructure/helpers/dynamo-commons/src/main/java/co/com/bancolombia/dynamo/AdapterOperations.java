package co.com.bancolombia.dynamo;

import co.com.bancolombia.dynamo.annotation.DynamoDbTableAdapter;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.lang.reflect.ParameterizedType;

import static reactor.core.publisher.Mono.fromFuture;

public class AdapterOperations<E, D> {

    private DynamoDbAsyncTable<D> table;
    private Class<E> entityClass;
    private Class<D> dataClass;
    private final ModelMapper mapper;

    public AdapterOperations(final DynamoDbEnhancedAsyncClient client) {
        ParameterizedType params = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.entityClass = (Class<E>) params.getActualTypeArguments()[0];
        this.dataClass = (Class<D>) params.getActualTypeArguments()[1];
        this.mapper = new ModelMapper();
        String tableName = dataClass.getAnnotation(DynamoDbTableAdapter.class).tableName();
        this.table = client.table(tableName, TableSchema.fromBean(dataClass));
    }

    protected Mono<Void> save(E entity) {
        return fromFuture(table.putItem(toData(entity)));
    }

    protected Mono<E> findById(String partitionKey) {
        return fromFuture(table.getItem(buildKey(partitionKey)))
                .map(this::toEntity);
    }

    protected Mono<E> update(E entity) {
        return fromFuture(table.putItem(toData(entity)))
                .then(findById("0002"));
    }

    protected Mono<Void> delete(String partitionKey) {
        return fromFuture(table.deleteItem(buildKey(partitionKey))).then();
    }

    private Key buildKey(String partitionKey) {
        return Key.builder()
                .partitionValue(partitionKey)
                .build();
    }

    private E toEntity(D data){
        return (data != null) ? mapper.map(data, entityClass) : null;
    }

    private D toData(E entity){
        return (entity != null) ? mapper.map(entity, dataClass) : null;
    }

}
