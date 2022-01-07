package co.com.bancolombia.config;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UseCasesConfigTest {

    @InjectMocks
    private UseCasesConfig useCasesConfig;

    @BeforeAll
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void useCasesConfigTest() {
        assertThat(useCasesConfig).isNotNull();
    }
}
