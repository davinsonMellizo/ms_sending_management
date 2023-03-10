package co.com.bancolombia.events.handlers;

import co.com.bancolombia.events.HandlerRegistryConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class HandlerRegistryConfigurationTest {
    @InjectMocks
    HandlerRegistryConfiguration configuration;
    @Mock
    CommandsHandler commandsHandler;
    @Mock
    EventsHandler eventsHandler;

    @BeforeEach
    public void init() {}

    @Test
    void handlerRegistry (){
        assertThat(configuration.handlerRegistry(commandsHandler,eventsHandler).getEventListeners()).isNotNull();
    }
}
