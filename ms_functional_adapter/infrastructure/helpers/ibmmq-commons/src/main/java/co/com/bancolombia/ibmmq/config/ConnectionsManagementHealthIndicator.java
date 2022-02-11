package co.com.bancolombia.ibmmq.config;

import co.com.bancolombia.ibmmq.jms.JmsManagement;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.jms.JmsHealthIndicator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ConnectionsManagementHealthIndicator implements HealthIndicator {
    private final List<JmsHealthIndicator> indicators;

    public ConnectionsManagementHealthIndicator(final JmsManagement cm) {
        indicators = cm.getConnectionData().getConnections()
                .stream()
                .map(conn -> cm.getConnectionFactory().connectionFactory(conn))
                .map(JmsHealthIndicator::new).collect(Collectors.toList());
    }

    @Override
    public Health health() {
        Health.Builder builder = new Health.Builder().up();
        Map<String, Object> details = new HashMap<>();
        indicators.forEach(indicator -> {
            Health health = indicator.getHealth(true);
            if (health.getStatus() == Status.DOWN) {
                builder.down();
            }
            details.put(indicator.toString(), health.getDetails());
        });
        builder.withDetails(details);
        return builder.build();
    }
}