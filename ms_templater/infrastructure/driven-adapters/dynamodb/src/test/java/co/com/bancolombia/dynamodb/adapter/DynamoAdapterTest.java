package co.com.bancolombia.dynamodb.adapter;

import co.com.bancolombia.dynamo.AdapterOperations;
import co.com.bancolombia.dynamodb.SampleData;
import co.com.bancolombia.model.template.dto.Template;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DynamoAdapterTest {

    @InjectMocks
    private DynamoAdapter dynamoAdapter;

    @Mock
    private DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

    @Mock
    private AdapterOperations adapterOperations;

    @BeforeAll
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void dynamoAdapterTest() {
        assertThat(dynamoAdapter).isNotNull();
    }

    @Test
    void templateGenerator() {
        StepVerifier.create(dynamoAdapter.templateGenerator(SampleData.templater()))
                .assertNext(template -> assertThat(template)
                        .isInstanceOf(Template.class)).verifyComplete();
    }
}
