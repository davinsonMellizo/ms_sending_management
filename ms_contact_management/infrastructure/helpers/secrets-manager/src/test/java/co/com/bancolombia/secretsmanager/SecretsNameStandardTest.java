package co.com.bancolombia.secretsmanager;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SecretsNameStandardTest {
    private static final String name = "";
    private final SecretsNameStandard secretsNameStandard= new SecretsNameStandard(name,name,name);

    @Test
    void secretForPostgresTest() {
        assertThat(secretsNameStandard.secretForPostgres()).isNotNull();
    }
    @Test
    void secretForRabbitMQTest() {
        assertThat(secretsNameStandard.secretForRabbitMQ()).isNotNull();
    }

    @Test
    void secretForPostgresReadTest() {
        assertThat(secretsNameStandard.secretForPostgresRead()).isNotNull();
    }
}
