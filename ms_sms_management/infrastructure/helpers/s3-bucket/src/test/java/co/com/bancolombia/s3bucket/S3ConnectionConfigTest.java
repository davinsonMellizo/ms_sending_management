package co.com.bancolombia.s3bucket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;
import software.amazon.awssdk.regions.Region;

import java.io.IOException;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class S3ConnectionConfigTest {
    @InjectMocks
    private S3ConnectionConfig s3ConnectionConfigMock;

    @Mock
    private S3ConnectionProperties s3ConnectionProperties;

    @BeforeEach
    public void init(){
        when(s3ConnectionProperties.getQueueCapacity()).thenReturn(10_000);
        when(s3ConnectionProperties.getCorePoolSize()).thenReturn(50);
        when(s3ConnectionProperties.getMaximumPoolSize()).thenReturn(50);
        when(s3ConnectionProperties.getKeepAliveTime()).thenReturn(10);
        when(s3ConnectionProperties.getRegion()).thenReturn(Region.US_EAST_1);
    }

    @Test
    void s3AsyncClientTest() throws IOException {
        Assert.notNull(s3ConnectionConfigMock.s3AsyncClient());
    }

    @Test
    void s3AsyncClientLocalTest() throws IOException {
        when(s3ConnectionProperties.getEndpoint()).thenReturn("http://localhost:4566");
        Assert.notNull(s3ConnectionConfigMock.s3AsyncClientLocal( ));
    }
}