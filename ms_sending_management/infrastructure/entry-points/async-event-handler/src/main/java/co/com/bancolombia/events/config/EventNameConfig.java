package co.com.bancolombia.events.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventNameConfig {

    public static final String SEND_CREATE_ISERIES = "send.create.client";
    public static final String SEND_UPDATE_ISERIES = "send.update.client";
}
