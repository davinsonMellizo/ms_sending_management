package co.com.bancolombia.events;

import co.com.bancolombia.commons.utils.JsonUtils;
import co.com.bancolombia.events.handlers.Handler;
import co.com.bancolombia.events.model.ResourceQuery;
import co.com.bancolombia.s3bucket.S3AsynOperations;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.async.api.HandlerRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class HandlerRegistryConfiguration {

    private final S3AsynOperations s3AsynOperations;
    @Value("${aws.s3.request-config-mq-key}")
    private String configMQKey;
    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Bean
    public HandlerRegistry queryHandler(Handler handler) {
        HandlerRegistry register = HandlerRegistry.register();

        ResourceQuery resourceQuery = JsonUtils.stringToType(
                s3AsynOperations.getFileAsString(bucketName, configMQKey).block(), ResourceQuery.class);

        resourceQuery.getData().forEach(resource -> handler.listenerMessage( resource, register));
        return register;
    }

}