import co.com.bancolombia.commons.exceptions.TechnicalException;
import org.junit.jupiter.api.Test;


import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.TECHNICAL_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;

public class TechnicalExceptionTest {


    TechnicalException technicalException = new TechnicalException(TECHNICAL_EXCEPTION);
    TechnicalException technicalExceptionTow = new TechnicalException(new Throwable("Error"),TECHNICAL_EXCEPTION);
    TechnicalException technicalExceptionMess = new TechnicalException("Error",TECHNICAL_EXCEPTION);
    TechnicalException technicalExceptionMessCode = new TechnicalException("Error",40);
    TechnicalException technicalExceptionMessCod = new TechnicalException("Error",TECHNICAL_EXCEPTION,40);


    @Test
    void  technicalExceptionTest (){
        assertThat(technicalException).isNotNull();
    }
    @Test
    void  TechnicalExceptionMessTest (){
        assertThat(technicalExceptionMess).isNotNull();
    }
    @Test
    void  TechnicalExceptionMessCodeTest (){
        assertThat(technicalExceptionMessCode).isNotNull();
    }
    @Test
    void  TechnicalExceptionMessCodTest (){
        assertThat(technicalExceptionMessCod).isNotNull();
    }
    @Test
    void  TechnicalExceptionTowTest (){
        assertThat(technicalExceptionTow).isNotNull();
    }

}
