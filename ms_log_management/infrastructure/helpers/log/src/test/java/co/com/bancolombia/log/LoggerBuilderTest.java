package co.com.bancolombia.log;

import co.com.bancolombia.model.commons.enums.TypeLog;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

public class LoggerBuilderTest {

    private static final String TRANSACTION_ID = "asdfghj-asdfghj-asdfghj";
    private static final String CHANNEL = "Any";
    private static final String SERVICE = "/service";
    private static final String DATA = "string any data";
    private LoggerBuilder loggerBuilder = new LoggerBuilder(CHANNEL);

    @Test
    public void givenErrorWhenValuesThenSuccessTest() {
        assertThatCode(() -> loggerBuilder.error(new Throwable(DATA), TRANSACTION_ID, CHANNEL, SERVICE))
                .doesNotThrowAnyException();
    }

    @Test
    public void errorTest() {
        assertThatCode(() -> loggerBuilder.error(new Throwable(DATA)))
                .doesNotThrowAnyException();
    }

    @Test
    public void givenInfoWhenValuesThenSuccessTest() {
        assertThatCode(() -> loggerBuilder.info(DATA, TRANSACTION_ID, CHANNEL, SERVICE, TypeLog.REQ))
                .doesNotThrowAnyException();

    }

    @Test
    public void givenInfoWhenIsMessageThenSuccessTest() {
        assertThatCode(() -> loggerBuilder.info(DATA))
                .doesNotThrowAnyException();
    }

}
