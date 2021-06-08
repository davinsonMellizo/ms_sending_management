package co.com.bancolombia.drivenadapters;


import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class TimeFactory {

    public LocalDateTime now() {
        return LocalDateTime.now(ZoneId.of("America/Bogota"));
    }
}
