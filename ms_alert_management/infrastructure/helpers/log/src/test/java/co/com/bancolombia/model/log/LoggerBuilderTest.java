package co.com.bancolombia.model.log;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

public class LoggerBuilderTest {

    private static final String TRANSACTION_ID = "asdfghj-asdfghj-asdfghj";
    private static final String CHANNEL = "Any";
    private static final String SERVICE = "/service";
    private static final String DATA = "string any data";
    private LoggerBuilder loggerBuilder = new LoggerBuilder(CHANNEL);

    @Test
    void givenErrorWhenValuesThenSuccessTest() {
        assertThatCode(() -> loggerBuilder.error(new Throwable(DATA), TRANSACTION_ID, CHANNEL, SERVICE))
                .doesNotThrowAnyException();
    }

    @Test
    void errorTest() {
        assertThatCode(() -> loggerBuilder.error(new Throwable(DATA)))
                .doesNotThrowAnyException();
    }

    @Test
    void givenInfoWhenValuesThenSuccessTest() {
        assertThatCode(() -> loggerBuilder.info(DATA, TRANSACTION_ID, CHANNEL, SERVICE))
                .doesNotThrowAnyException();

    }

    @Test
    void givenInfoWhenIsMessageThenSuccessTest() {
        assertThatCode(() -> loggerBuilder.info(DATA))
                .doesNotThrowAnyException();
    }

}
