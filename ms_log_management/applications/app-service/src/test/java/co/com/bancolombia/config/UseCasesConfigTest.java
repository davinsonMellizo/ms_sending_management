package co.com.bancolombia.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UseCasesConfigTest {

    @InjectMocks
    private UseCasesConfig config;
    @Test
    void customOpenAPITest(){
        assertThat(config.customOpenAPI("1")).isNotNull();
    }
}
