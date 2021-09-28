package co.com.bancolombia.model.log;


import co.com.bancolombia.commons.enums.TypeLog;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.logging.technical.logger.TechLogger;
import co.com.bancolombia.logging.technical.message.ObjectTechMsg;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class LoggerBuilder {

    private final TechLogger logger;

    public LoggerBuilder(@Value("${spring.application.name}") String appName) {
        this.logger = LoggerFactory.getLog(appName);
    }

    public void error(Throwable error, String transactionId, String channel, String service) {
        logger.error(buildError(error, transactionId, channel, service));
    }

    public void error(Throwable error) {
        logger.error(ObjectTechMsg.builder()
                .message(buildStackTrace(error))
                .build());
    }

    public <T> void info(T data) {
        logger.info(ObjectTechMsg.builder()
                .message(data)
                .build());
    }

    public <T> void info(T data, String transactionId, String service, String channel, TypeLog type) {
        logger.info(buildObject(data, transactionId, service, channel, type.getValue()));
    }

    private ObjectTechMsg<?> buildError(Throwable error, String transactionId, String channel, String service) {
        return buildObject(buildStackTrace(error), transactionId, channel, service);
    }

    private <T> ObjectTechMsg<?> buildObject(T message, String transactionId, String service, String... tags) {
        return ObjectTechMsg.builder()
                .serviceName(service)
                .transactionId(transactionId)
                .tagList(Arrays.asList(tags))
                .message(message)
                .build();
    }

    private Map<String, Object> buildStackTrace(Throwable error) {
        Map<String, Object> map = new HashMap<>();
        Optional.ofNullable(error.getStackTrace()).ifPresent(trace -> map.put("stackTrace", trace));
        Optional.ofNullable(error.getMessage()).ifPresent(message -> map.put("message", message));
        map.put("exception", error.toString());
        return map;
    }

}
