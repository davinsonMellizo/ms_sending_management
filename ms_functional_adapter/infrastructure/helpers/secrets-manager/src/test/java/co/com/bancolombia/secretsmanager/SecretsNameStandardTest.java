package co.com.bancolombia.secretsmanager;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SecretsNameStandardTest {
    private final SecretsNameStandard secretsNameStandard= new SecretsNameStandard("","");

    @Test
    void secretForPostgres() {
        assertThat(secretsNameStandard.secretForPostgres()).isNotNull();
    }
    @Test
    void secretForRabbitMQ() {
        assertThat(secretsNameStandard.secretForRabbitMQ()).isNotNull();
    }
}
