package co.com.bancolombia.events.commons;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventNameConfig {
    public static final String SEND_ALERT = "send.alert.sms";
    public static final String SEND_ALERT_RETRY = "send.alert.sms.dlq";
}
