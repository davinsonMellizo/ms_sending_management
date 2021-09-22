package co.com.bancolombia.category;

import co.com.bancolombia.drivenadapters.TimeFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class TestAPP {
    public static void main(String[] args) {
        run(TestAPP.class, args);
    }

    @Bean
    public TimeFactory timeFactory() {
        return new TimeFactory();
    }
}
