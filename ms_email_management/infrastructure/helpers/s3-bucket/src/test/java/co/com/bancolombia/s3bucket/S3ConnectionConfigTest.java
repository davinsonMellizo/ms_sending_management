package co.com.bancolombia.s3bucket;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class S3ConnectionConfigTest {
    @InjectMocks
    private S3ConnectionConfig s3ConnectionConfigMock;

    @Mock
    private S3ConnectionProperties s3ConnectionProperties;

    @Mock
    private S3Presigner s3Presigner;


    @Test
    void s3AsyncClientTest() throws IOException {
        when(s3ConnectionProperties.getQueueCapacity()).thenReturn(10_000);
        when(s3ConnectionProperties.getCorePoolSize()).thenReturn(50);
        when(s3ConnectionProperties.getMaximumPoolSize()).thenReturn(50);
        when(s3ConnectionProperties.getKeepAliveTime()).thenReturn(10);
        when(s3ConnectionProperties.getRegion()).thenReturn(Region.US_EAST_1);
        assertThat(s3ConnectionConfigMock.s3AsyncClient()).isNotNull();
    }

    @Test
    void s3AsyncClientLocalTest() throws IOException {
        when(s3ConnectionProperties.getQueueCapacity()).thenReturn(10_000);
        when(s3ConnectionProperties.getCorePoolSize()).thenReturn(50);
        when(s3ConnectionProperties.getMaximumPoolSize()).thenReturn(50);
        when(s3ConnectionProperties.getKeepAliveTime()).thenReturn(10);
        when(s3ConnectionProperties.getRegion()).thenReturn(Region.US_EAST_1);
        when(s3ConnectionProperties.getEndpoint()).thenReturn("http://localhost:4566");
        assertThat(s3ConnectionConfigMock.s3AsyncClientLocal()).isNotNull();
    }

    @Test
    void s3PresignerTest() {
        assertThat(s3ConnectionConfigMock.s3Presigner()).isNotNull();
    }
}