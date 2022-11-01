package co.com.bancolombia.api.commons;

import co.com.bancolombia.api.dto.TemplateDTO;
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
        TemplateDTO templateDTO = new TemplateDTO();
        templateDTO.setIdTemplate("001");
        templateDTO.setMessageType("Email");
        templateDTO.setMessageSubject("Alertas y Notificaciones");
        templateDTO.setMessageBody("Body");
        templateDTO.setPlainText("Text");
        templateDTO.setUser("User");
        templateDTO.setIdConsumer("ConsumerId");
        Assertions.assertDoesNotThrow(() -> requestValidator.validateBody(templateDTO));
    }

    @Test
    void validateBodyThrowException() {
        Validator myValidator = Validation.buildDefaultValidatorFactory().getValidator();
        TemplateDTO templateDTO = new TemplateDTO();
        templateDTO.setIdTemplate("");
        templateDTO.setMessageType("Email");
        templateDTO.setMessageSubject("Alertas y Notificaciones");
        templateDTO.setMessageBody("Body");
        templateDTO.setPlainText("Text");
        templateDTO.setUser("User");
        templateDTO.setIdConsumer("ConsumerId");
        Set<ConstraintViolation<TemplateDTO>> constrain = myValidator.validate(templateDTO);
        Mockito.when(validator.validate(templateDTO)).thenReturn(constrain);
        Assertions.assertThrows(TechnicalException.class, () ->
                requestValidator.validateBody(templateDTO));
    }
}
