package co.com.bancolombia.events.handlers;

import co.com.bancolombia.events.HandlerRegistryConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class HandlerRegistryConfigurationTest {
    @InjectMocks
    HandlerRegistryConfiguration configuration;
    @Mock
    CommandsHandler commandsHandler;

    @BeforeEach
    public void init() {}

    @Test
    void handlerRegistry (){
        assertThat(configuration.handlerRegistry(commandsHandler).getEventListeners()).isNotNull();
    }


}
