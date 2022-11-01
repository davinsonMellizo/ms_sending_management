package co.com.bancolombia.consumer.config;

import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.s3bucket.S3AsynOperations;
import co.com.bancolombia.secretsmanager.SecretsManager;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
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
    private final S3AsynOperations s3AsynOperations;
    private final AwsProperties awsProperties;
    private final LoggerBuilder logger;

    private ClientHttpConnector clientHttpConnectorWithSsl(int timeout) {

        KeyStore truststore = null;

        try {
            var propertiesSsl = secretManager
                    .getSecret(awsProperties.getNameSecretBucketSsl(),PropertiesSsl.class).block();

            assert propertiesSsl != null;

            InputStream cert = s3AsynOperations
                    .getFileAsInputStream(awsProperties.getS3().getBucket() ,propertiesSsl.getKeyStore()).block();
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
        } catch (NoSuchAlgorithmException | KeyStoreException | CertificateException | IOException e){
            logger.error(e);
        }
        return null;
    }

    private ClientHttpConnector clientHttpConnectorWithoutSsl(int timeout) {
        SslContext sslContext = null;
        try {
            sslContext = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

        } catch (SSLException e) {
            throw new AssertionError(e);
        }

        var finalSslContext = sslContext;
        return new ReactorClientHttpConnector(HttpClient.create()
                .secure(t -> t.sslContext(finalSslContext))
                .tcpConfiguration(tcpClient -> tcpClient.option(CONNECT_TIMEOUT_MILLIS, timeout)));
    }

    @Bean
    @Primary
    public WebClient webClientConfig(final ConsumerProperties consumerProperties) {
        return WebClient.builder()
                .clientConnector(clientHttpConnectorWithSsl(consumerProperties.getTimeout()))
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean(name = "INA")
    @Autowired
    public WebClient webClientConfigIna(final ConsumerProperties consumerProperties) {
        return WebClient.builder()
                .clientConnector(getClientHttpConnector(consumerProperties.getTimeout()))
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
                .build();
    }

    private ClientHttpConnector getClientHttpConnector(int timeout) {
        return new ReactorClientHttpConnector(HttpClient.create()
                .compress(true)
                .keepAlive(true)
                .option(CONNECT_TIMEOUT_MILLIS, timeout)
        );
    }

}
