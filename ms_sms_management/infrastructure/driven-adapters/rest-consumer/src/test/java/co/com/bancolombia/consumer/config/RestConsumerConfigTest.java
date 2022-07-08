package co.com.bancolombia.consumer.config;

import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.s3bucket.S3AsynOperations;
import co.com.bancolombia.secretsmanager.SecretsManager;
import io.netty.handler.ssl.SslContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class RestConsumerConfigTest {

    @InjectMocks
    private RestConsumerConfig restConsumerConfig;
    
    @Mock
    private SecretsManager secretsManager;
    @Mock
    private AwsProperties awsProperties;
    @Mock
    private S3AsynOperations s3AsynOperations;
    @Mock
    private SslContext sslContext;
    @Mock
    private LoggerBuilder loggerBuilder;

    private static final InputStream inputStream
            = new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8));



    @BeforeEach
    public void init() {

        MockitoAnnotations.initMocks(this);
        ClientHttpConnector cert = new ReactorClientHttpConnector(HttpClient.create()
                .tcpConfiguration(tcpClient -> {
                    tcpClient = tcpClient.option(CONNECT_TIMEOUT_MILLIS, 3600);
                    return tcpClient;
                }));
        when(secretsManager.getSecret(anyString(),any()))
                .thenReturn(Mono.just(PropertiesSsl.builder()
                        .keyStore("masivapp-com.jks")
                        .password("testPassword")
                        .build()));
        when(awsProperties.getNameSecretBucketSsl())
                .thenReturn("testNameSecret");
        when(s3AsynOperations.getFileAsInputStream(anyString(),anyString()))
                .thenReturn(Mono.just(inputStream));
        when(awsProperties.getS3())
                .thenReturn(new AwsProperties.S3("endpointTest","bucketTest"));
    }

    @Test
    void ConfigMASTest()  {
        assertThat(restConsumerConfig.webClientConfig(new ConsumerProperties(3600, null))).isNotNull();
    }
    @Test
    void configINATest() throws SSLException {
        assertThat(restConsumerConfig.webClientConfigIna(new ConsumerProperties(3600, null))).isNotNull();
    }
    @org.junit.Test(expected = SSLException.class)
    public void configINAErrorTest() throws SSLException {

        SSLException e =new SSLException("TEst");
        restConsumerConfig.webClientConfigIna(new ConsumerProperties(3600, null));
        doThrow(e).when(sslContext);



    }
}
