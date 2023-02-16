package co.com.bancolombia.consumer.config;

import co.com.bancolombia.d2b.model.secret.SyncSecretVault;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.s3bucket.S3AsyncOperations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class RestConsumerConfigTest {
    @InjectMocks
    private RestConsumerConfig restConsumerConfig;
    @Mock
    private SyncSecretVault secretsManager;
    @Mock
    private AwsProperties awsProperties;
    @Mock
    private S3AsyncOperations s3AsyncOperations;
    @Mock
    private LoggerBuilder loggerBuilder;

    private static final InputStream inputStream
            = new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8));


    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        ClientHttpConnector cert = new ReactorClientHttpConnector(HttpClient.create()
                .tcpConfiguration(tcpClient -> {
                    tcpClient = tcpClient.option(CONNECT_TIMEOUT_MILLIS, 3600);
                    return tcpClient;
                }));
        when(secretsManager.getSecret(anyString(), any()))
                .thenReturn(PropertiesSsl.builder()
                        .keyStore("masivapp-com.jks")
                        .password("testPassword")
                        .build());
        when(awsProperties.getNameSecretBucketSsl())
                .thenReturn("testNameSecret");
        when(s3AsyncOperations.getFileAsInputStream(anyString(), anyString()))
                .thenReturn(Mono.just(inputStream));
        when(awsProperties.getS3())
                .thenReturn(new AwsProperties.S3("endpointTest", "bucketTest"));

    }

    @Test
    void sesConfigTest() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        assertThat(restConsumerConfig.webClientConfig(new ConsumerProperties(3600, null))).isNotNull();
    }
}
