package co.com.bancolombia.events.handlers;

import co.com.bancolombia.events.HandlerRegistryConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
@ExtendWith(MockitoExtension.class)
class HandlerRegistryConfigurationTest {

    @InjectMocks
    private HandlerRegistryConfiguration configuration;
    @Mock
    private CommandsHandler handler;
    @Test
    void handlerRegistry() {
        assertThat(configuration.handlerRegistry(handler).getEventListeners()).isNotNull();
    }
}