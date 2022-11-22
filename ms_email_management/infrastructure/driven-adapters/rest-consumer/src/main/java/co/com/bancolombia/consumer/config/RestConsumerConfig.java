package co.com.bancolombia.consumer.config;

import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.s3bucket.S3AsyncOperations;
import co.com.bancolombia.secretsmanager.SecretsManager;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.TrustManagerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@RequiredArgsConstructor
public class RestConsumerConfig {
    private static final String TLS = "TLSv1.2";

    private final SecretsManager secretManager;
    private final S3AsyncOperations s3AsyncOperations;
    private final AwsProperties awsProperties;
    private final LoggerBuilder logger;

    private ClientHttpConnector clientHttpConnectorWithSsl(int timeout) {

        KeyStore truststore = null;
        try {
            var propertiesSsl = secretManager
                    .getSecret(awsProperties.getNameSecretBucketSsl(), PropertiesSsl.class).block();

            assert propertiesSsl != null;

            InputStream cert = s3AsyncOperations
                    .getFileAsInputStream(awsProperties.getS3().getBucket(), propertiesSsl.getKeyStore()).block();
            truststore = KeyStore.getInstance(KeyStore.getDefaultType());
            truststore.load(cert, propertiesSsl.getPassword().toCharArray());
            var trustManagerFactory = TrustManagerFactory.getInstance(TLS);
            trustManagerFactory.init(truststore);
            var sslContext = SslContextBuilder.forClient()
                    .trustManager(trustManagerFactory).protocols(TLS)
                    .build();
            return new ReactorClientHttpConnector(HttpClient.create()
                    .secure(t -> t.sslContext(sslContext))
                    .tcpConfiguration(tcpClient -> tcpClient.option(CONNECT_TIMEOUT_MILLIS, timeout)));
        } catch (NoSuchAlgorithmException | KeyStoreException | CertificateException | IOException e) {
            logger.error(e);
        }

        return null;
    }

    @Bean
    public WebClient webClientConfig(final ConsumerProperties consumerProperties) {
        return WebClient.builder()
                .clientConnector(clientHttpConnectorWithSsl(consumerProperties.getTimeout()))
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
                .build();
    }

}
