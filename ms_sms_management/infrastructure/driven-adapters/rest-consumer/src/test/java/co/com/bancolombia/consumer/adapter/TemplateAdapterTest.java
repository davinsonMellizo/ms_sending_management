package co.com.bancolombia.consumer.adapter;


import co.com.bancolombia.consumer.RestClient;
import co.com.bancolombia.consumer.adapter.response.SuccessTemplate;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TemplateAdapterTest {

    @InjectMocks
    private TemplateAdapter templateAdapter;

    @Mock
    private ConsumerProperties properties;

    @Mock
    private RestClient<Alert, SuccessTemplate> client;

    private SuccessTemplate.Dat dat= new SuccessTemplate.Dat();
    private Alert alert = new Alert();



    @BeforeEach
    public void init(){
        String url = "localhost";
        when(properties.getResources())
                .thenReturn(new ConsumerProperties.Resources(url,url,url,url,url,url,url,url,url));
        dat.setIdTemplate("template");
        dat.setPlainText("HTML");
        dat.setMessageSubject("EMAIL");
        alert.setProvider("MAS");
        Map <String, String> parameters = new HashMap<>();
        parameters.put("name", "bancolombia");
        alert.setTemplate(new Template( parameters, "Compra"));
        alert.setTrackId(UUID.randomUUID().toString());
    }

    @Test
    void findTemplateEmailTest (){
        when(client.requestGet(anyString(),any(),anyMap(),any(),any()))
                .thenReturn(Mono.just(SuccessTemplate.builder()
                        .data(dat).build()));
        StepVerifier
                .create(templateAdapter.findTemplateEmail(alert))
                .assertNext(response-> response.getBodyText().equals("HTML"))
                .verifyComplete();

    }

    @Test
    void findTemplateEmailErrorTest (){
        when(client.requestGet(anyString(),any(),anyMap(),any(),any()))
                .thenReturn(Mono.error(new Throwable()));
        StepVerifier
                .create(templateAdapter.findTemplateEmail(alert))
                .expectError()
                .verify();
    }

}
