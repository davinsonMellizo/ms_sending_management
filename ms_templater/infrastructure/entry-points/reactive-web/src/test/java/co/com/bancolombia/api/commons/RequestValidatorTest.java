package co.com.bancolombia.api.commons;

import co.com.bancolombia.api.dto.TemplaterDTO;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RequestValidatorTest {

    @InjectMocks
    private RequestValidator requestValidator;

    @Mock
    private Validator validator;

    @BeforeAll
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateBodyOk() {
        TemplaterDTO templaterDTO = new TemplaterDTO();
        templaterDTO.setId("001");
        templaterDTO.setMessageType("Email");
        templaterDTO.setMessageSubject("Alertas y Notificaciones");
        templaterDTO.setMessageBody("Body");
        templaterDTO.setMessageText("Text");
        templaterDTO.setCreationUser("User");
        templaterDTO.setConsumerId("ConsumerId");
        Assertions.assertDoesNotThrow(() -> requestValidator.validateBody(templaterDTO));
    }

    @Test
    void validateBodyThrowException() {
        Validator myValidator = Validation.buildDefaultValidatorFactory().getValidator();
        TemplaterDTO templaterDTO = new TemplaterDTO();
        templaterDTO.setId("");
        templaterDTO.setMessageType("Email");
        templaterDTO.setMessageSubject("Alertas y Notificaciones");
        templaterDTO.setMessageBody("Body");
        templaterDTO.setMessageText("Text");
        templaterDTO.setCreationUser("User");
        templaterDTO.setConsumerId("ConsumerId");
        Set<ConstraintViolation<TemplaterDTO>> constrain = myValidator.validate(templaterDTO);
        Mockito.when(validator.validate(templaterDTO)).thenReturn(constrain);
        Assertions.assertThrows(TechnicalException.class, () ->
                requestValidator.validateBody(templaterDTO));
    }
}
