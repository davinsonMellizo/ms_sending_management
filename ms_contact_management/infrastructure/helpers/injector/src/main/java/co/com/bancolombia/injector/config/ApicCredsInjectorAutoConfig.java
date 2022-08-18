package co.com.bancolombia.injector.config;

import co.com.bancolombia.d2b.model.secret.AsyncSecretVault;
import co.com.bancolombia.injector.ApicAuthFilterFunction;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

@Log4j2
@Configuration
public class ApicCredsInjectorAutoConfig {

    @Bean
    public ExchangeFilterFunction apicCredsFilterFunction(AsyncSecretVault asyncSecretVault,
                                                            @Value("${d2b.secrets.apic}") String apicSecretName) {
        log.info("D2B SDK - ApicCredsInjector - Creating exchange filter function");
        return new ApicAuthFilterFunction(asyncSecretVault, apicSecretName);
    }
}
