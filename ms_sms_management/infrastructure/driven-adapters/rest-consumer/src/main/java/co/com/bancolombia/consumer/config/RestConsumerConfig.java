package co.com.bancolombia.consumer.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;


import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class RestConsumerConfig {
    private static final String TLS = "TLSv1.3";

    private ClientHttpConnector clientHttpConnectorWithSsl(int timeout) throws KeyStoreException, IOException,
            NoSuchAlgorithmException, CertificateException {

        Path truststorePath = Paths.get("/path/to/your/truststore");
        InputStream truststoreInputStream = Files.newInputStream(truststorePath, StandardOpenOption.READ);

        KeyStore truststore = KeyStore.getInstance(KeyStore.getDefaultType());
        truststore.load(truststoreInputStream, "truststorePassword".toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TLS);
        trustManagerFactory.init(truststore);

        SslContext sslContext = SslContextBuilder.forClient()
                .trustManager(trustManagerFactory).protocols(TLS)
                .build();

        return new ReactorClientHttpConnector(HttpClient.create()
                .secure(t -> t.sslContext(sslContext))
                .tcpConfiguration(tcpClient -> tcpClient.option(CONNECT_TIMEOUT_MILLIS, timeout)));
    }

    private ClientHttpConnector clientHttpConnectorWithoutSsl(int timeout) {
        return new ReactorClientHttpConnector(HttpClient.create()
                .tcpConfiguration(tcpClient -> {
                    tcpClient = tcpClient.option(CONNECT_TIMEOUT_MILLIS, timeout);
                    return tcpClient;
                }));
    }

    @Bean
    public WebClient webClientConfig(final ConsumerProperties consumerProperties) {
        return WebClient.builder()
                .clientConnector(clientHttpConnectorWithoutSsl(consumerProperties.getTimeout()))
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
                .build();
    }

}