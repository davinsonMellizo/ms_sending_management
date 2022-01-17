package co.com.bancolombia.secretsmanager;

import co.com.bancolombia.secretsmanager.api.GenericManager;
import co.com.bancolombia.secretsmanager.api.exceptions.SecretException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class SecretsHelper<T, R> {

    private static final String OK_MSG = "Secret {} loaded successfully";

    private static final Logger LOGGER = LoggerFactory.getLogger(SecretsHelper.class.getName());

    protected String secretName;
    protected Class<T> clazz;

    protected SecretsHelper(final Class<T> clazz, final String secretName) {
        this.clazz = clazz;
        this.secretName = secretName;
    }

    protected R createConfigFromSecret(final GenericManager manager,
                                       final Function<T, R> configMaker) throws SecretException {
        final R configInstance = configMaker.apply(manager.getSecret(secretName, clazz));
        LOGGER.info(OK_MSG, secretName);
        return configInstance;
    }

}
