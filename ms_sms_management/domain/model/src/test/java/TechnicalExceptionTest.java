import co.com.bancolombia.commons.exceptions.TechnicalException;
import org.junit.jupiter.api.Test;


import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.TECHNICAL_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;

class TechnicalExceptionTest {


    TechnicalException technicalException = new TechnicalException(TECHNICAL_EXCEPTION);
    TechnicalException technicalExceptionTow = new TechnicalException(new Throwable("Error"),TECHNICAL_EXCEPTION);
    TechnicalException technicalExceptionMess = new TechnicalException("Error",TECHNICAL_EXCEPTION);
    TechnicalException technicalExceptionMessCode = new TechnicalException("Error",40);
    TechnicalException technicalExceptionMessCod = new TechnicalException("Error",TECHNICAL_EXCEPTION,40);


    @Test
    void  technicalETest (){
        assertThat(technicalException).isNotNull();
    }
    @Test
    void  TechnicalEMessTest (){
        assertThat(technicalExceptionMess).isNotNull();
    }
    @Test
    void  TechnicalEMessCodeTest (){
        assertThat(technicalExceptionMessCode).isNotNull();
    }
    @Test
    void  TechnicalEMessCodTest (){
        assertThat(technicalExceptionMessCod).isNotNull();
    }
    @Test
    void  TechnicalETowTest (){
        assertThat(technicalExceptionTow).isNotNull();
    }

}
